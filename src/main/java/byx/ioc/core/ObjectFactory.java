package byx.ioc.core;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 对象工厂：封装对象实例化和初始化逻辑
 *
 * @author byx
 */
public interface ObjectFactory {
    /**
     * 实例化
     * @return 实例化的对象
     */
    Object doCreate();

    /**
     * 初始化
     * @param obj 实例化后的对象
     */
    void doInit(Object obj);

    /**
     * 对象类型
     * @return 类型
     */
    Class<?> getType();

    /**
     * 创建对象工厂
     * @param create 实例化方法
     * @param init 初始化方法
     * @param type 对象类型
     * @return 对象工厂
     */
    static ObjectFactory of(Supplier<Object> create, Consumer<Object> init, Class<?> type) {
        return new ObjectFactory() {
            @Override
            public Object doCreate() {
                return create.get();
            }

            @Override
            public void doInit(Object obj) {
                init.accept(obj);
            }

            @Override
            public Class<?> getType() {
                return type;
            }
        };
    }

    /**
     * 创建对象工厂
     * @param create 初始化方法
     * @param type 对象类型
     * @return 对象工厂
     */
    static ObjectFactory of(Supplier<Object> create, Class<?> type) {
        return of(create, obj -> {}, type);
    }
}
