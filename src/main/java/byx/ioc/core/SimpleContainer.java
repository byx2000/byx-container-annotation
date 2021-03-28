package byx.ioc.core;

import byx.ioc.exception.IdDuplicatedException;
import byx.ioc.exception.IdNotFoundException;
import byx.ioc.exception.MultiTypeMatchException;
import byx.ioc.exception.TypeNotFoundException;

import java.util.*;

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

    /**
     * 存储每一种类型的对象一共有多少个
     */
    private final Map<Class<?>, Integer> typeCount = new HashMap<>();

    /**
     * 缓存已实例化的对象，用于解决循环依赖
     */
    private final Map<String, Object> cache = new HashMap<>();

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

    /**
     * 检查指定类型的对象是否存在且唯一
     */
    private void checkTypeExistAndUnique(Class<?> type) {
        if (!typeCount.containsKey(type)) {
            throw new TypeNotFoundException(type);
        } else if (typeCount.get(type) > 1) {
            throw new MultiTypeMatchException(type);
        }
    }

    @Override
    public void registerObject(String id, ObjectFactory factory) {
        // 检查id是否重复
        checkIdDuplicated(id);

        // 添加ObjectFactory
        factories.put(id, factory);

        // 更新类型计数
        Class<?> type = factory.getType();
        if (typeCount.containsKey(type)) {
            int currentCount = typeCount.get(type);
            typeCount.put(type, currentCount + 1);
        } else {
            typeCount.put(type, 1);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(String id) {
        // 检查指定id的对象是否存在
        checkIdExist(id);

        // 尝试从缓存中取
        if (cache.containsKey(id)) {
            return (T) cache.get(id);
        }

        // 如果缓存中没有，则通过ObjectFactory创建
        return (T) createObject(id, factories.get(id));
    }

    @Override
    public <T> T getObject(Class<T> type) {
        // 检查指定类型的对象是否存在且唯一
        checkTypeExistAndUnique(type);

        // 尝试从缓存中取
        for (Object obj : cache.values()) {
            if (type.isAssignableFrom(obj.getClass())) {
                return type.cast(obj);
            }
        }

        // 如果缓存中没有，则通过ObjectFactory创建
        for (String id : factories.keySet()) {
            ObjectFactory factory = factories.get(id);
            if (type.isAssignableFrom(factory.getType())) {
                return type.cast(createObject(id, factory));
            }
        }

        // 理论上代码不会执行到这里
        throw new TypeNotFoundException(type);
    }

    @Override
    public Set<String> getObjectIds() {
        return factories.keySet();
    }

    /**
     * 使用ObjectFactory创建对象
     */
    private Object createObject(String id, ObjectFactory factory) {
        // 实例化对象
        Object obj = factory.doCreate();

        // 如果当前对象已经在缓存里了，说明在上一步的实例化过程中已经创建了当前对象
        // 所以此处直接返回缓存中的对象
        if (cache.containsKey(id)) {
            obj = cache.get(id);
        }

        // 将已实例化但未初始化的对象加入缓存
        cache.put(id, obj);

        // 初始化对象
        factory.doInit(obj);

        return obj;
    }
}
