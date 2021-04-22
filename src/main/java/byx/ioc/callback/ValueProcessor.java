package byx.ioc.callback;

import byx.ioc.annotation.Value;
import byx.ioc.annotation.Values;
import byx.ioc.converter.ValueConverter;
import byx.ioc.core.Container;
import byx.ioc.core.ContainerCallback;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.exception.ValueConverterNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 处理Value注解
 *
 * @author byx
 */
public class ValueProcessor implements ContainerCallback {
    @Override
    public void afterContainerInit(Container container) {
        // 获取所有类上标注的Value注解
        List<Value> values = container.getObjectTypes().stream()
                .filter(t -> t.isAnnotationPresent(Values.class))
                .map(t -> t.getAnnotation(Values.class))
                .flatMap(vs -> Arrays.stream(vs.value()))
                .collect(Collectors.toList());

        // 获取容器中的所有类型转换器
        Set<ValueConverter> converters = container.getObjects(ValueConverter.class);

        // 添加自带的转换器
        converters.add(ValueConverter.STRING_CONVERTER);
        converters.add(ValueConverter.INT_CONVERTER);
        converters.add(ValueConverter.DOUBLE_CONVERTER);
        converters.add(ValueConverter.CHAR_CONVERTER);
        converters.add(ValueConverter.BOOLEAN_CONVERTER);

        for (Value v : values) {
            Class<?> type = v.type();
            String valueStr = v.value();
            String id = v.id();

            // 查找合适的转换器，并执行转换操作
            Object obj = null;
            boolean find = false;
            for (ValueConverter converter : converters) {
                if (type == converter.getType()) {
                    obj = converter.convert(valueStr);
                    find = true;
                    break;
                }
            }

            // 找不到转换器
            if (!find) {
                throw new ValueConverterNotFoundException(type);
            }

            // 注册对象
            Object finalObj = obj;
            container.registerObject("".equals(id) ? valueStr : id, new ObjectDefinition() {
                @Override
                public Class<?> getType() {
                    return type;
                }

                @Override
                public Object getInstance(Object[] params) {
                    return finalObj;
                }
            });
        }
    }
}
