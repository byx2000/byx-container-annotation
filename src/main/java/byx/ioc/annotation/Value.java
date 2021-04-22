package byx.ioc.annotation;

import java.lang.annotation.*;

/**
 * 向容器中注册常量值
 *
 * @author byx
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Values.class)
public @interface Value {
    Class<?> type() default String.class;
    String id() default "";
    String value();
}
