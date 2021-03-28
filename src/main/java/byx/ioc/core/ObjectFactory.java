package byx.ioc.core;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ObjectFactory {
    Object doCreate();
    void doInit(Object obj);
    Class<?> getType();

    static ObjectFactory of(Supplier<Object> instantiate, Consumer<Object> initialization, Class<?> type) {
        return new ObjectFactory() {
            @Override
            public Object doCreate() {
                return instantiate.get();
            }

            @Override
            public void doInit(Object obj) {
                initialization.accept(obj);
            }

            @Override
            public Class<?> getType() {
                return type;
            }
        };
    }

    static ObjectFactory of(Supplier<Object> instantiate, Class<?> type) {
        return of(instantiate, obj -> {}, type);
    }
}
