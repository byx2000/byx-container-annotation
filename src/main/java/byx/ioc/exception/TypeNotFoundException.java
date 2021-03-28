package byx.ioc.exception;

/**
 * 找不到指定类型的对象
 *
 * @author byx
 */
public class TypeNotFoundException extends ByxContainerException {
    public TypeNotFoundException(Class<?> type) {
        super("There is no object of " + type + " in container.");
    }
}
