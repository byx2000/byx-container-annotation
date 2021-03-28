package byx.ioc.core;

import java.util.Set;

/**
 * IOC容器
 *
 * @author byx
 */
public interface Container {
    /**
     * 注册对象
     * @param id id
     * @param factory 对象工厂
     */
    void registerObject(String id, ObjectFactory factory);

    /**
     * 获取指定id的对象
     * @param id id
     * @param <T> 对象类型
     * @return 对象
     */
    <T> T getObject(String id);

    /**
     * 获取指定类型的对象
     * @param type 对象类型
     * @param <T> 对象类型
     * @return 对象
     */
    <T> T getObject(Class<T> type);

    /**
     * 获取容器中所有对象id的集合
     * @return id集合
     */
    Set<String> getObjectIds();
}
