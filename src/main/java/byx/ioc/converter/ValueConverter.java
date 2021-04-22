package byx.ioc.converter;

import java.util.function.Function;

/**
 * 类型转换器：把String转换为指定类型
 * 与Value注解配合使用
 *
 * @author byx
 */
public interface ValueConverter {
    /**
     * 获取转换后的类型
     * @return 类型
     */
    Class<?> getType();

    /**
     * 执行转换后的操作
     * @param s 字符串
     * @return 转换后的对象
     */
    Object convert(String s);

    /**
     * 创建一个ValueConverter
     */
    static ValueConverter of(Class<?> type, Function<String, Object> convert) {
        return new ValueConverter() {
            @Override
            public Class<?> getType() {
                return type;
            }

            @Override
            public Object convert(String s) {
                return convert.apply(s);
            }
        };
    }

    /**
     * 自带转换器
     */
    ValueConverter STRING_CONVERTER = of(String.class, s -> s);
    ValueConverter INT_CONVERTER = of(int.class, Integer::valueOf);
    ValueConverter DOUBLE_CONVERTER = of(double.class, Double::valueOf);
    ValueConverter CHAR_CONVERTER = of(char.class, s -> s.charAt(0));
    ValueConverter BOOLEAN_CONVERTER = of(boolean.class, Boolean::valueOf);
}
