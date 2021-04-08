package byx.ioc.factory;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;
import byx.ioc.core.*;
import byx.ioc.exception.ByxAopNotFoundException;
import byx.ioc.exception.ConstructorMultiDefException;
import byx.ioc.exception.ConstructorNotFoundException;
import byx.ioc.util.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ContainerFactory的实现类：通过扫描注解来创建IOC容器
 *
 * @author byx
 */
public class AnnotationContainerFactory implements ContainerFactory {
    private final String packageName;

    private static final String AOP_PROXY_CREATOR = "byx.aop.ByxAOP";
    private static final String AOP_PROXY_CREATOR_METHOD = "getAopProxy";

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
        // 获取实例化的构造函数
        Constructor<?> constructor = getConstructor(type);

        // 处理依赖解析
        Supplier<Dependency[]> getDependencies = processDependencies(constructor);

        // 处理初始化
        Consumer<Object> initialization = processInit(type, container);

        // 处理AOP
        Function<Object, Object> wrap = processWrap(type, container);

        // 处理方法
        for (Method method : type.getMethods()) {
            if (method.isAnnotationPresent(Component.class)) {
                processMethod(type, method, container);
            }
        }

        // 创建对象工厂
        ObjectDefinition factory = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return type;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return getDependencies.get();
            }

            @Override
            public Object getInstance(Object[] params) {
                try {
                    return constructor.newInstance(params);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void doInit(Object obj) {
                initialization.accept(obj);
            }

            @Override
            public Object doWrap(Object obj) {
                return wrap.apply(obj);
            }
        };

        // 获取id
        String id = type.isAnnotationPresent(Id.class)
                ? type.getAnnotation(Id.class).value()
                : type.getCanonicalName();

        // 注册对象工厂
        container.registerObject(id, factory);
    }

    private static Constructor<?> getConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getConstructors();
        Constructor<?> constructor;

        if (constructors.length == 1) {
            constructor = constructors[0];
        } else {
            constructors = Arrays.stream(constructors)
                    .filter(c -> c.isAnnotationPresent(Autowire.class))
                    .toArray(Constructor[]::new);
            if (constructors.length == 0) {
                throw new ConstructorNotFoundException(type);
            } else if (constructors.length > 1) {
                throw new ConstructorMultiDefException(type);
            } else {
                constructor = constructors[0];
            }
        }

        return constructor;
    }

    private static Supplier<Dependency[]> processDependencies(Constructor<?> constructor) {
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

        // 返回依赖获取函数
        return () -> {
            Dependency[] dependencies = new Dependency[paramTypes.length];
            for (int i = 0; i < dependencies.length; ++i) {
                if (paramIds[i] != null) {
                    dependencies[i] = Dependency.id(paramIds[i]);
                } else {
                    dependencies[i] = Dependency.type(paramTypes[i]);
                }
            }
            return dependencies;
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
        ObjectDefinition factory = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return method.getReturnType();
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                Dependency[] dependencies = new Dependency[paramTypes.length];
                for (int i = 0; i < dependencies.length; ++i) {
                    if (paramIds[i] != null) {
                        dependencies[i] = Dependency.id(paramIds[i]);
                    } else {
                        dependencies[i] = Dependency.type(paramTypes[i]);
                    }
                }
                return dependencies;
            }

            @Override
            public Object getInstance(Object[] params) {
                Object instance = (instanceId == null) ? container.getObject(instanceType) : container.getObject(instanceId);
                try {
                    method.setAccessible(true);
                    return method.invoke(instance, params);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // 获取id
        String id = method.isAnnotationPresent(Id.class)
                ? method.getAnnotation(Id.class).value()
                : method.getName();

        // 注册对象工厂
        container.registerObject(id, factory);
    }

    private static Method loadByxAopProxyMethod() {
        try {
            return Class.forName(AOP_PROXY_CREATOR).getMethod(AOP_PROXY_CREATOR_METHOD, Object.class, Object.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new ByxAopNotFoundException();
        }
    }

    private static Function<Object, Object> processWrap(Class<?> type, Container container) {
        if (type.isAnnotationPresent(AdviceBy.class)) {
            Class<?> adviceType = type.getAnnotation(AdviceBy.class).value();
            Method method = loadByxAopProxyMethod();
            return obj -> {
                try {
                    return method.invoke(null, obj, container.getObject(adviceType));
                } catch (IllegalAccessException e) {
                    throw new ByxAopNotFoundException();
                } catch (InvocationTargetException e) {
                    // 如果ByxAOP抛出了RuntimeException，则直接向外抛出
                    if (e.getTargetException() instanceof RuntimeException) {
                        throw (RuntimeException) e.getTargetException();
                    } else {
                        throw new ByxAopNotFoundException();
                    }
                }
            };
        }
        return obj -> obj;
    }
}
