package byx.ioc.factory;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;
import byx.ioc.core.Container;
import byx.ioc.core.Dependency;
import byx.ioc.core.ExtendableContainerFactory;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.exception.ConstructorMultiDefException;
import byx.ioc.exception.ConstructorNotFoundException;
import byx.ioc.util.AnnotationScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * ContainerFactory的实现类：通过注解扫描来创建IOC容器
 *
 * @author byx
 */
public class AnnotationContainerFactory extends ExtendableContainerFactory {
    /**
     * 基准包名
     */
    private final String basePackage;

    /**
     * 创建一个AnnotationContainerFactory
     * 扫描basePackage包及其子包下的所有类
     * @param basePackage 基准包名
     */
    public AnnotationContainerFactory(String basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 创建一个AnnotationContainerFactory
     * 扫描baseClass所在的包及其子包下的所有类
     * @param baseClass 基类
     */
    public AnnotationContainerFactory(Class<?> baseClass) {
        this.basePackage = baseClass.getPackageName();
    }

    @Override
    protected void initContainer(Container container) {
        // 扫描包下所有标注了Component注解的类
        new AnnotationScanner(basePackage)
                .getClassesAnnotatedBy(Component.class)
                .forEach(c -> processClass(c, container));
    }

    private static void processClass(Class<?> type, Container container) {
        // 获取实例化的构造函数
        Constructor<?> constructor = getConstructor(type);

        // 获取实例化的依赖项
        Dependency[] dependencies = processDependencies(constructor);

        // 创建ObjectDefinition
        ObjectDefinition definition = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return type;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return dependencies;
            }

            @Override
            public Object getInstance(Object[] params) {
                try {
                    return constructor.newInstance(params);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // 获取id
        String id = type.isAnnotationPresent(Id.class)
                ? type.getAnnotation(Id.class).value()
                : type.getCanonicalName();

        // 注册对象
        container.registerObject(id, definition);
    }

    /**
     * 获取用于对象实例化的构造函数
     */
    private static Constructor<?> getConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getConstructors();
        Constructor<?> constructor;

        // 如果只有唯一的构造方法，则直接使用这个构造方法
        // 如果有多个构造方法，则使用标注了Autowired注解的构造方法
        // 其它情况则报错
        if (constructors.length == 1) {
            constructor = constructors[0];
        } else {
            constructors = Arrays.stream(constructors)
                    .filter(c -> c.isAnnotationPresent(Autowired.class))
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

    /**
     * 构造函数依赖项的获取
     */
    private static Dependency[] processDependencies(Constructor<?> constructor) {
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

        // 解析依赖项
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
}
