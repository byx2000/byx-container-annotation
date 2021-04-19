package byx.ioc.callback;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;
import byx.ioc.core.Container;
import byx.ioc.core.ContainerCallback;
import byx.ioc.core.Dependency;
import byx.ioc.core.ObjectDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * 处理实例工厂组件的注册
 *
 * @author byx
 */
public class MethodComponentProcessor implements ContainerCallback {
    @Override
    public void afterContainerInit(Container container) {
        Set<String> ids = container.getObjectIds();
        for (String id : ids) {
            ObjectDefinition definition = container.getObjectDefinition(id);
            Class<?> type = definition.getType();

            Arrays.stream(type.getMethods())
                    .filter(m -> m.isAnnotationPresent(Component.class))
                    .forEach(m -> processMethod(type, m, container));
        }
    }

    private void processMethod(Class<?> instanceType, Method method, Container container) {
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

        // 解析依赖项
        Dependency[] dependencies = new Dependency[paramTypes.length];
        for (int i = 0; i < dependencies.length; ++i) {
            if (paramIds[i] != null) {
                dependencies[i] = Dependency.id(paramIds[i]);
            } else {
                dependencies[i] = Dependency.type(paramTypes[i]);
            }
        }

        // 创建ObjectDefinition
        ObjectDefinition definition = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return method.getReturnType();
            }

            @Override
            public Dependency[] getInstanceDependencies() {
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
        container.registerObject(id, definition);
    }
}
