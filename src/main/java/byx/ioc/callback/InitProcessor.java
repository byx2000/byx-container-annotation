package byx.ioc.callback;

import byx.ioc.annotation.Id;
import byx.ioc.annotation.Init;
import byx.ioc.core.Container;
import byx.ioc.core.ObjectCallback;
import byx.ioc.core.ObjectCallbackContext;
import byx.ioc.exception.MultiInitMethodDefException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理init方法
 *
 * @author byx
 */
public class InitProcessor implements ObjectCallback {
    @Override
    public void afterObjectInit(ObjectCallbackContext ctx) {
        Object obj = ctx.getObject();
        Class<?> type = obj.getClass();
        Container container = ctx.getContainer();

        // 查找标注了Init注解的方法
        List<Method> methods = Arrays.stream(type.getMethods())
                .filter(m -> m.isAnnotationPresent(Init.class))
                .collect(Collectors.toList());

        if (methods.size() == 0) {
            return;
        }

        // 存在多个被Init注解标注的方法，直接报错
        if (methods.size() > 1) {
            throw new MultiInitMethodDefException(type);
        }

        Method method = methods.get(0);
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            boolean hasId = false;
            for (Annotation a : paramAnnotations[i]) {
                if (a instanceof Id) {
                    String id = ((Id) a).value();
                    params[i] = container.getObject(id);
                    hasId = true;
                    break;
                }
            }

            if (!hasId) {
                params[i] = container.getObject(paramTypes[i]);
            }
        }

        method.setAccessible(true);
        try {
            method.invoke(obj, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getOrder() {
        // 确保在AutowiredProcessor之后执行
        return Integer.MIN_VALUE + 1;
    }
}
