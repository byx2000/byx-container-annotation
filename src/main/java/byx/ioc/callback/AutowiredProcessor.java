package byx.ioc.callback;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Id;
import byx.ioc.core.Container;
import byx.ioc.core.ObjectCallback;
import byx.ioc.core.ObjectCallbackContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理Autowired注入
 *
 * @author byx
 */
public class AutowiredProcessor implements ObjectCallback {
    @Override
    public void afterObjectInit(ObjectCallbackContext ctx) {
        Object obj = ctx.getObject();
        Container container = ctx.getContainer();

        processFields(obj, container);
        processSetters(obj, container);
    }

    /**
     * 处理字段注入
     */
    private void processFields(Object obj, Container container) {
        Class<?> type = obj.getClass();

        // 获取所有需要被注入的字段
        List<Field> autoWireFields = new ArrayList<>();
        List<String> autoWireIds = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                autoWireFields.add(field);
                String id = field.isAnnotationPresent(Id.class)
                        ? field.getAnnotation(Id.class).value()
                        : null;
                autoWireIds.add(id);
            }
        }

        // 反射注入字段
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
    }

    /**
     * 处理setter注入
     */
    private void processSetters(Object obj, Container container) {
        Class<?> type = obj.getClass();

        // 获取所有标注了Autowired的方法
        List<Method> methods = Arrays.stream(type.getMethods())
                .filter(m -> m.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList());

        // 遍历每个方法
        // 获取方法参数及其注入类型
        // 从容器获取实参值，然后传入方法并调用
        for (Method method : methods) {
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
    }

    @Override
    public int getOrder() {
        // 确保最先执行
        return Integer.MIN_VALUE;
    }
}
