package byx.ioc.exception;

/**
 * 多个被Create注解的构造函数
 *
 * @author byx
 */
public class ConstructorMultiDefException extends ByxContainerException {
    public ConstructorMultiDefException(Class<?> type) {
        super("There is more than one constructor with @Create annotation in class: " + type.getCanonicalName());
    }
}
