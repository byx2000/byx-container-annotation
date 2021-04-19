package byx.ioc.callback;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Id;
import byx.ioc.core.Container;
import byx.ioc.core.ObjectCallback;
import byx.ioc.core.ObjectCallbackContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理Autowired注入
 *
 * @author byx
 */
public class AutowiredProcessor implements ObjectCallback {
    @Override
    public void afterObjectInit(ObjectCallbackContext ctx) {
        Object obj = ctx.getObject();
        Class<?> type = obj.getClass();
        Container container = ctx.getContainer();

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

    @Override
    public int getOrder() {
        // 确保最先执行
        return Integer.MIN_VALUE;
    }
}
