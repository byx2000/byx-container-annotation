package byx.ioc.factory;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;
import byx.ioc.core.Container;
import byx.ioc.core.ContainerFactory;
import byx.ioc.core.ObjectFactory;
import byx.ioc.core.SimpleContainer;
import byx.ioc.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class AnnotationContainerFactory implements ContainerFactory {
    private final String packageName;

    public AnnotationContainerFactory(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public Container create() {
        List<Class<?>> classes = ReflectUtils.getPackageClasses(packageName);

        Container container = new SimpleContainer();
        for (Class<?> c : classes) {
            if (c.isAnnotationPresent(Component.class)) {
                if (c.isAnnotationPresent(Id.class)) {
                    Id id = c.getAnnotation(Id.class);
                    container.registerObject(id.value(), createByConstructor(c, container));
                } else {
                    container.registerObject(c.getCanonicalName(), createByConstructor(c, container));
                }

                for (Method method : c.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Component.class)) {
                        if (method.isAnnotationPresent(Id.class)) {
                            Id id = c.getAnnotation(Id.class);
                            container.registerObject(id.value(), createByMethod(c, method, container));
                        } else {
                            container.registerObject(method.getName(), createByMethod(c, method, container));
                        }
                    }
                }
            }
        }

        return container;
    }

    private static ObjectFactory createByConstructor(Class<?> type, Container container) {
        Constructor<?>[] constructors = type.getConstructors();
        Constructor<?> constructor = constructors[0];
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
        String[] paramIds = new String[paramTypes.length];

        for (int i = 0; i < paramTypes.length; ++i) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof Id) {
                    paramIds[i] = ((Id) a).value();
                }
            }
        }

        return new ObjectFactory() {
            @Override
            public Object doInstantiate() {
                Object[] params = new Object[paramTypes.length];
                for (int i = 0; i < params.length; ++i) {
                    if (paramIds[i] != null) {
                        params[i] = container.getObject(paramIds[i]);
                    } else {
                        params[i] = container.getObject(paramTypes[i]);
                    }
                }

                try {
                    return constructor.newInstance(params);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void doInitialization(Object obj) {

            }

            @Override
            public Class<?> getType() {
                return type;
            }
        };
    }

    private static ObjectFactory createByMethod(Class<?> instanceType, Method method, Container container) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        String[] paramIds = new String[paramTypes.length];

        for (int i = 0; i < paramTypes.length; ++i) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof Id) {
                    paramIds[i] = ((Id) a).value();
                }
            }
        }

        String instanceId = instanceType.isAnnotationPresent(Id.class)
                ? instanceType.getAnnotation(Id.class).value()
                : null;

        return new ObjectFactory() {
            @Override
            public Object doInstantiate() {
                Object[] params = new Object[paramTypes.length];
                for (int i = 0; i < params.length; ++i) {
                    if (paramIds[i] != null) {
                        params[i] = container.getObject(paramIds[i]);
                    } else {
                        params[i] = container.getObject(paramTypes[i]);
                    }
                }

                Object instance = (instanceId == null) ? container.getObject(instanceType) : container.getObject(instanceId);

                try {
                    return method.invoke(instance, params);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void doInitialization(Object obj) {

            }

            @Override
            public Class<?> getType() {
                return method.getReturnType();
            }
        };
    }
}
