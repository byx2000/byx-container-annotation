package byx.ioc.core;

import java.util.Set;

public interface Container {
    void registerObject(String id, ObjectFactory factory);
    <T> T getObject(String id);
    <T> T getObject(Class<T> type);
    Set<String> getObjectIds();
}
