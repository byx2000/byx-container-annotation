package byx.ioc.core;

import byx.ioc.exception.IdDuplicatedException;
import byx.ioc.exception.IdNotFoundException;
import byx.ioc.exception.MultiTypeMatchException;
import byx.ioc.exception.TypeNotFoundException;

import java.util.*;

public class SimpleContainer implements Container {
    private final Map<String, ObjectFactory> factories = new HashMap<>();
    private final Map<String, Object> idCache = new HashMap<>();
    private final Map<Class<?>, Set<Object>> typeCache = new HashMap<>();

    @Override
    public void registerObject(String id, ObjectFactory factory) {
        if (factories.containsKey(id)) {
            throw new IdDuplicatedException(id);
        }
        factories.put(id, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(String id) {
        if (idCache.containsKey(id)) {
            return (T) idCache.get(id);
        }
        if (factories.containsKey(id)) {
            return (T) createObject(id, factories.get(id));
        }
        throw new IdNotFoundException(id);
    }

    @Override
    public <T> T getObject(Class<T> type) {
        if (typeCache.containsKey(type)) {
            Set<Object> set = typeCache.get(type);
            if (set.size() >= 1) {
                return type.cast(set.iterator().next());
            }
        }

        T obj = null;
        boolean found = false;

        for (String id : factories.keySet()) {
            ObjectFactory factory = factories.get(id);
            if (type.isAssignableFrom(factory.getType())) {
                if (found) {
                    throw new MultiTypeMatchException(type);
                }
                found = true;
                obj = type.cast(createObject(id, factory));
            }
        }

        if (!found) {
            throw new TypeNotFoundException(type);
        }

        return obj;
    }

    @Override
    public Set<String> getObjectIds() {
        return factories.keySet();
    }

    private void cacheObject(String id, Class<?> type, Object obj) {
        idCache.put(id, obj);
        if (typeCache.containsKey(type)) {
            typeCache.get(type).add(obj);
        } else {
            Set<Object> set = new HashSet<>();
            set.add(obj);
            typeCache.put(type, set);
        }
    }

    private Object createObject(String id, ObjectFactory factory) {
        Object obj = factory.doCreate();
        if (idCache.containsKey(id)) {
            obj = idCache.get(id);
        }
        cacheObject(id, factory.getType(), obj);
        factory.doInit(obj);
        return obj;
    }
}
