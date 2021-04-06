package byx.ioc.core;

/**
 * 对象工厂：封装对象实例化和初始化逻辑
 *
 * @author byx
 */
public interface ObjectFactory {
    /**
     * 对象类型
     * @return 类型
     */
    Class<?> getType();

    /**
     * 获取实例化时的依赖项
     * @return 实例化过程中用到的参数数组
     */
    default Dependency[] getCreateDependencies() {
        return new Dependency[0];
    }

    /**
     * 实例化
     * @param params getCreateDependencies返回的参数数组
     * @return 实例化的对象
     */
    Object doCreate(Object[] params);

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
