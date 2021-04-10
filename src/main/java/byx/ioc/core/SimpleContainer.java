package byx.ioc.core;

import byx.ioc.exception.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Container的实现类：支持循环依赖的IOC容器
 *
 * @author byx
 */
public class SimpleContainer implements Container {
    /**
     * 存储所有ObjectDefinition
     */
    private final Map<String, ObjectDefinition> definitions = new HashMap<>();

    /**
     * 一级缓存：存放已完全初始化的对象
     */
    private final Map<String, Object> cache1 = new HashMap<>();

    /**
     * 二级缓存：存放已实例化对象的工厂
     * 该工厂包含对已实例化对象的代理操作
     * 通过调用ObjectDefinition的doWrap方法
     */
    private final Map<String, Supplier<Object>> cache2 = new HashMap<>();

    /**
     * 当第一次调用getObject方法之后，禁止再向容器中注册对象
     */
    private boolean freeze = false;

    /**
     * 检查id是否重复
     */
    private void checkIdDuplicated(String id) {
        if (definitions.containsKey(id)) {
            throw new IdDuplicatedException(id);
        }
    }

    /**
     * 检查id是否存在
     */
    private void checkIdExist(String id) {
        if (!definitions.containsKey(id)) {
            throw new IdNotFoundException(id);
        }
    }

    /**
     * 第一次调用getObject方法时冻结整个容器，并检测循环依赖
     */
    private void checkCircularDependencyAndFreezeContainer() {
        if (!freeze) {
            checkCircularDependency();
            freeze = true;
        }
    }

    @Override
    public void registerObject(String id, ObjectDefinition definition) {
        if (!freeze) {
            checkIdDuplicated(id);
            definitions.put(id, definition);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(String id) {
        checkCircularDependencyAndFreezeContainer();
        checkIdExist(id);
        return (T) createOrGetObject(id, definitions.get(id));
    }

    @Override
    public <T> T getObject(Class<T> type) {
        List<String> candidates = definitions.keySet().stream()
                .filter(id -> type.isAssignableFrom(definitions.get(id).getType()))
                .collect(Collectors.toList());

        if (candidates.size() == 0) {
            throw new TypeNotFoundException(type);
        }
        if (candidates.size() > 1) {
            throw new MultiTypeMatchException(type);
        }

        return getObject(candidates.get(0));
    }

    @Override
    public <T> T getObject(String id, Class<T> type) {
        Object obj = getObject(id);
        if (!type.isAssignableFrom(obj.getClass())) {
            throw new TypeNotFoundException(type);
        }
        return type.cast(obj);
    }

    @Override
    public Set<String> getObjectIds() {
        return definitions.keySet();
    }

    /**
     * 创建依赖项
     */
    private Object createDependency(Dependency dependency) {
        if (dependency.getId() != null) {
            return getObject(dependency.getId());
        } else if (dependency.getType() != null) {
            return getObject(dependency.getType());
        }
        throw new BadDependencyException(dependency);
    }

    /**
     * 创建依赖数组
     */
    private Object[] createDependencies(Dependency[] dependencies) {
        Object[] params = new Object[dependencies.length];
        for (int i = 0; i < dependencies.length; ++i) {
            params[i] = createDependency(dependencies[i]);
        }
        return params;
    }

    /**
     * 创建/获取容器中的对象
     * 循环依赖处理的核心算法
     *
     * 对象创建步骤：
     * 1. 查找一级缓存，如果找到则直接返回
     * 2. 查找二级缓存，如果找到，则取出对象工厂，执行代理操作，然后把代理后的对象移入一级缓存，
     *    并返回代理后的对象
     * 3. 如果两级缓存都没找到，说明对象是第一次创建，依次执行下面的步骤：
     *      1) 调用ObjectDefinition的getInstanceDependencies方法，获取对象实例化所需的依赖项
     *      2) 递归调用Container的getObject方法创建依赖项
     *      3) 调用ObjectDefinition的getInstance方法创建对象实例
     *      4) 使用对象工厂包装刚刚创建的对象实例，工厂内部调用ObjectDefinition的doWrap方法创建代理
     *      5) 把对象工厂放入二级缓存
     *      6) 调用ObjectDefinition的doInit方法初始化对象（属性填充）
     *      7) 再次尝试从缓存中获取对象，这次一定能够获取到
     */
    private Object createOrGetObject(String id, ObjectDefinition definition) {
        // 查找一级缓存，如果找到则直接返回
        if (cache1.containsKey(id)) {
            return cache1.get(id);
        }

        // 查找二级缓存，如果找到则调用get方法，然后把二级缓存移动到一级缓存
        if (cache2.containsKey(id)) {
            Object obj = cache2.get(id).get();
            cache2.remove(id);
            cache1.put(id, obj);
            return obj;
        }

        // 获取并创建对象实例化的依赖项
        Object[] params = createDependencies(definition.getInstanceDependencies());

        // 查找一级缓存和二级缓存，如果找到则直接返回
        if (cache1.containsKey(id)) {
            return cache1.get(id);
        }
        if (cache2.containsKey(id)) {
            Object obj = cache2.get(id).get();
            cache2.remove(id);
            cache1.put(id, obj);
            return obj;
        }

        // 实例化对象
        Object obj = definition.getInstance(params);

        // 将实例化后的对象加入二级缓存
        cache2.put(id, () -> definition.doWrap(obj));

        // 初始化对象
        definition.doInit(obj);

        return createOrGetObject(id, definition);
    }

    private String getTypeId(Class<?> type) {
        List<String> candidates = definitions.keySet().stream()
                .filter(id -> type.isAssignableFrom(definitions.get(id).getType()))
                .collect(Collectors.toList());
        if (candidates.size() != 1) {
            return null;
        }
        return candidates.get(0);
    }

    /**
     * 循环依赖检测
     *
     * 步骤：
     * 1. 调用容器中所有ObjectDefinition的getInstanceDependencies，获取所有对象的实例化依赖项，
     *    将依赖关系转换成一张有向图
     * 2. 使用拓扑排序的顺序依次删除图中的节点，直到不能再删为止
     * 3. 如果还存在未删除的节点，说明依赖图中存在环路，即产生了循环依赖
     */
    private void checkCircularDependency() {
        // 初始化邻接表矩阵
        int n = definitions.size();
        boolean[][] adj = new boolean[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                adj[i][j] = false;
            }
        }

        // 获取容器中所有对象id
        List<String> ids = new ArrayList<>(definitions.keySet());

        // 构建对象的构造函数依赖图
        for (int i = 0; i < n; ++i) {
            String id = ids.get(i);
            Dependency[] dependencies = definitions.get(id).getInstanceDependencies();
            for (Dependency dep : dependencies) {
                if (dep.getId() != null) {
                    int j = ids.indexOf(dep.getId());
                    adj[i][j] = true;
                } else if (dep.getType() != null) {
                    int j = ids.indexOf(getTypeId(dep.getType()));
                    adj[i][j] = true;
                } else {
                    throw new BadDependencyException(dep);
                }
            }
        }

        // in存储所有节点的入度
        // all集合存储当前还未排序的节点编号
        // ready集合存储当前入度为0的节点
        int[] in = new int[n];
        Set<Integer> all = new HashSet<>();
        List<Integer> ready = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            all.add(i);
            in[i] = 0;
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (adj[i][j]) {
                    in[j]++;
                }
            }
        }
        for (int i = 0; i < n; ++i) {
            if (in[i] == 0) {
                ready.add(i);
            }
        }

        // 按照拓扑排序的顺序删除节点
        while (!ready.isEmpty()) {
            int cur = ready.remove(0);
            all.remove(cur);
            for (int i = 0; i < n; ++i) {
                if (adj[cur][i]) {
                    in[i]--;
                    if (in[i] == 0) {
                        ready.add(i);
                    }
                }
            }
        }

        // 如果还有未排序的节点，说明依赖图中存在环路，即发生了循环依赖
        if (!all.isEmpty()) {
            // 获取构成循环依赖的对象id
            List<String> circularIds = new ArrayList<>();
            for (int i : all) {
                circularIds.add(ids.get(i));
            }
            throw new CircularDependencyException(circularIds);
        }
    }
}
