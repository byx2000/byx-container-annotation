package byx.ioc.core;

import byx.ioc.exception.IdDuplicatedException;
import byx.ioc.exception.IdNotFoundException;
import byx.ioc.exception.MultiTypeMatchException;
import byx.ioc.exception.TypeNotFoundException;

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
     * 存储所有ObjectFactory，ObjectFactory封装了对象的实例化和初始化操作
     */
    private final Map<String, ObjectFactory> factories = new HashMap<>();

    private final Map<String, Object> cache1 = new HashMap<>();
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
     * 使用ObjectFactory创建对象
     */
    private Object createObject(String id, ObjectFactory factory) {
        // 获取实例化的依赖
        /*Object[] params = factory.getCreateDependencies();
        // 如果当前对象已经在缓存里了，说明在上一步的实例化过程中已经创建了当前对象
        // 所以此处直接返回缓存中的对象


        // 实例化对象
        Object obj;
        obj = factory.doCreate(params);

        if (cache.containsKey(id)) {
            obj = cache.get(id);
        }

        // 将已实例化但未初始化的对象加入缓存
        cache.put(id, obj);

        // 初始化对象
        factory.doInit(obj);

        return obj;*/

        if (cache1.containsKey(id)) {
            return cache1.get(id);
        }

        if (cache2.containsKey(id)) {
            Object obj = cache2.get(id).get();
            cache2.remove(id);
            cache1.put(id, obj);
            return obj;
        }

        Object[] params = factory.getCreateDependencies();

        if (cache1.containsKey(id)) {
            return cache1.get(id);
        }
        if (cache2.containsKey(id)) {
            Object obj = cache2.get(id).get();
            cache2.remove(id);
            cache1.put(id, obj);
            return obj;
        }

        Object obj = factory.doCreate(params);
        cache2.put(id, () -> factory.doWrap(obj));

        factory.doInit(obj);

        return getObject(id);
    }
}
