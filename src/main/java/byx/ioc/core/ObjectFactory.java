package byx.ioc.core;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    default Object[] getCreateDependencies() {
        return null;
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

    /*Supplier<Object[]> EMPTY_DEPENDENCIES = () -> null;
    Consumer<Object> EMPTY_INIT = obj -> {};
    Function<Object, Object> EMPTY_WRAP = obj -> obj;

    static ObjectFactory of(Function<Object[], Object> create, Class<?> type) {
        return new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return type;
            }

            @Override
            public Object doCreate(Object[] params) {
                return create.apply(params);
            }
        };
    }

    static ObjectFactory of(Supplier<Object> create, Class<?> type) {
        return new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return type;
            }

            @Override
            public Object doCreate(Object[] params) {
                return create.get();
            }
        };
    }

    static ObjectFactory of(Supplier<Object> create, Consumer<Object> init, Class<?> type) {
        return new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return type;
            }

            @Override
            public Object doCreate(Object[] params) {
                return create.get();
            }

            @Override
            public void doInit(Object obj) {
                init.accept(obj);
            }
        };
    }

    static ObjectFactory of(Supplier<Object[]> getDependencies, Function<Object[], Object> create, Consumer<Object> init, Function<Object, Object> wrap, Class<?> type) {
        return new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return type;
            }

            @Override
            public Object[] getCreateDependencies() {
                return getDependencies.get();
            }

            @Override
            public Object doCreate(Object[] params) {
                return create.apply(params);
            }

            @Override
            public void doInit(Object obj) {
                init.accept(obj);
            }

            @Override
            public Object doWrap(Object obj) {
                return wrap.apply(obj);
            }
        };
    }*/

    /*static ObjectFactory of(Supplier<Object[]> getDependencies, Function<Object[], Object> create, Class<?> type) {
        return of(getDependencies, create, EMPTY_INIT, EMPTY_WRAP, type);
    }*/
}
