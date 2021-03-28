package byx.ioc.factory;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Create;
import byx.ioc.annotation.Id;
import byx.ioc.core.Container;
import byx.ioc.core.ContainerFactory;
import byx.ioc.core.ObjectFactory;
import byx.ioc.core.SimpleContainer;
import byx.ioc.exception.ConstructorMultiDefException;
import byx.ioc.exception.ConstructorNotFoundException;
import byx.ioc.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * ContainerFactory的实现类：通过扫描注解来创建IOC容器
 *
 * @author byx
 */
public class AnnotationContainerFactory implements ContainerFactory {
    private final String packageName;

    public AnnotationContainerFactory(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public Container create() {
        // 获取包下的所有类
        List<Class<?>> classes = ReflectUtils.getPackageClasses(packageName);

        // 创建容器
        Container container = new SimpleContainer();

        // 扫描包下所有类
        for (Class<?> c : classes) {
            if (c.isAnnotationPresent(Component.class)) {
                processClass(c, container);
            }
        }

        return container;
    }

    private static void processClass(Class<?> type, Container container) {
        // 处理实例化
        Supplier<Object> instantiate = processCreate(type, container);

        // 处理初始化
        Consumer<Object> initialization = processInit(type, container);

        // 处理方法
        for (Method method : type.getMethods()) {
            if (method.isAnnotationPresent(Component.class)) {
                processMethod(type, method, container);
            }
        }

        // 创建对象工厂
        ObjectFactory factory = ObjectFactory.of(instantiate, initialization, type);

        // 获取id
        String id = type.isAnnotationPresent(Id.class)
                ? type.getAnnotation(Id.class).value()
                : type.getCanonicalName();

        // 注册对象工厂
        container.registerObject(id, factory);
    }

    private static Supplier<Object> processCreate(Class<?> type, Container container) {
        // 获取用于实例化对象的构造函数
        Constructor<?>[] constructors = type.getConstructors();
        Constructor<?> constructor;
        if (constructors.length == 1) {
            constructor = constructors[0];
        } else {
            constructors = Arrays.stream(constructors)
                    .filter(c -> c.isAnnotationPresent(Create.class))
                    .toArray(Constructor[]::new);
            if (constructors.length == 0) {
                throw new ConstructorNotFoundException(type);
            } else if (constructors.length > 1) {
                throw new ConstructorMultiDefException(type);
            } else {
                constructor = constructors[0];
            }
        }

        // 获取构造函数参数的注入类型
        Class<?>[] paramTypes = constructor.getParameterTypes();

        // 获取构造函数参数的注入id
        Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
        String[] paramIds = new String[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof Id) {
                    paramIds[i] = ((Id) a).value();
                }
            }
        }

        // 返回实例化函数
        Constructor<?> finalConstructor = constructor;
        return () -> {
            Object[] params = new Object[paramTypes.length];
            for (int i = 0; i < params.length; ++i) {
                if (paramIds[i] != null) {
                    params[i] = container.getObject(paramIds[i]);
                } else {
                    params[i] = container.getObject(paramTypes[i]);
                }
            }

            try {
                return finalConstructor.newInstance(params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static Consumer<Object> processInit(Class<?> type, Container container) {
        // 获取所有需要被赋值的字段
        List<Field> autoWireFields = new ArrayList<>();
        List<String> autoWireIds = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowire.class)) {
                autoWireFields.add(field);
                String id = field.isAnnotationPresent(Id.class)
                        ? field.getAnnotation(Id.class).value()
                        : null;
                autoWireIds.add(id);
            }
        }

        // 返回字段初始化函数
        return obj -> {
            for (int i = 0; i < autoWireFields.size(); ++i) {
                Field field = autoWireFields.get(i);
                field.setAccessible(true);
                String id = autoWireIds.get(i);
                try {
                    if (id == null) {
                        field.set(obj, container.getObject(field.getType()));
                    } else {
                        field.set(obj, container.getObject(id));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private static void processMethod(Class<?> instanceType, Method method, Container container) {
        // 获取方法参数注入类型
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        
        // 获取方法参数注入id
        String[] paramIds = new String[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof Id) {
                    paramIds[i] = ((Id) a).value();
                }
            }
        }

        // 获取方法所属的对象实例id
        String instanceId = instanceType.isAnnotationPresent(Id.class)
                ? instanceType.getAnnotation(Id.class).value()
                : null;

        // 创建对象工厂
        ObjectFactory factory = ObjectFactory.of(() -> {
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
                method.setAccessible(true);
                return method.invoke(instance, params);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, method.getReturnType());

        // 获取id
        String id = method.isAnnotationPresent(Id.class)
                ? method.getAnnotation(Id.class).value()
                : method.getName();

        // 注册对象工厂
        container.registerObject(id, factory);
    }
}
