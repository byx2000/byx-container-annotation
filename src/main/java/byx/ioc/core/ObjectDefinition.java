package byx.ioc.core;

/**
 * 封装对象实例化、初始化和包装逻辑
 *
 * @author byx
 */
public interface ObjectDefinition {
    /**
     * 对象类型
     * @return 类型
     */
    Class<?> getType();

    /**
     * 获取实例化时的依赖项
     * @return 实例化过程中用到的参数数组
     */
    default Dependency[] getInstanceDependencies() {
        return new Dependency[0];
    }

    /**
     * 实例化
     * @param params 实例化依赖项
     * @return 实例化的对象
     */
    Object getInstance(Object[] params);

    /**
     * 初始化
     * @param obj 实例化后的对象
     */
    default void doInit(Object obj) {

    }

    /**
     * 包装对象
     * @param obj 初始化后的对象
     * @return 包装后的对象
     */
    default Object doWrap(Object obj) {
        return obj;
    }
}
