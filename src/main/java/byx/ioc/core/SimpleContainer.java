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
     * 存储所有ObjectFactory
     */
    private final Map<String, ObjectFactory> factories = new HashMap<>();

    /**
     * 一级缓存：存放已完全初始化的对象
     */
    private final Map<String, Object> cache1 = new HashMap<>();

    /**
     * 二级缓存：存放已实例化对象的工厂，该工厂包含对已实例化对象的代理操作
     */
    private final Map<String, Supplier<Object>> cache2 = new HashMap<>();

    /**
     * 检查id是否重复
     */
    private void checkIdDuplicated(String id) {
        if (factories.containsKey(id)) {
            throw new IdDuplicatedException(id);
        }
    }

    /**
     * 检查id是否存在
     */
    private void checkIdExist(String id) {
        if (!factories.containsKey(id)) {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public void registerObject(String id, ObjectFactory factory) {
        // 检查id是否重复
        checkIdDuplicated(id);

        // 添加ObjectFactory
        factories.put(id, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(String id) {
        // 检查指定id的对象是否存在
        checkIdExist(id);

        // 如果缓存中没有，则通过ObjectFactory创建
        return (T) createObject(id, factories.get(id));
    }

    @Override
    public <T> T getObject(Class<T> type) {
        List<String> candidates = factories.keySet().stream()
                .filter(id -> type.isAssignableFrom(factories.get(id).getType()))
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
    public Set<String> getObjectIds() {
        return factories.keySet();
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
     * 使用ObjectFactory创建对象
     */
    private Object createObject(String id, ObjectFactory factory) {
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
        Object[] params = createDependencies(factory.getCreateDependencies());

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
        Object obj = factory.doCreate(params);

        // 将实例化后的对象加入二级缓存
        cache2.put(id, () -> factory.doWrap(obj));

        // 初始化对象
        factory.doInit(obj);

        return getObject(id);
    }
}
