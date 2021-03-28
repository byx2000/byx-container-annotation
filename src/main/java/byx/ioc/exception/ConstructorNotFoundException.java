package byx.ioc.exception;

/**
 * 找不到构造函数
 *
 * @author byx
 */
public class ConstructorNotFoundException extends ByxContainerException {
    public ConstructorNotFoundException(Class<?> type) {
        super("Cannot find properly constructor in class " + type.getCanonicalName());
    }
}
