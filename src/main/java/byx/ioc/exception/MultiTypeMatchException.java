package byx.ioc.exception;

/**
 * 找到多个指定类型的对象
 *
 * @author byx
 */
public class MultiTypeMatchException extends ByxContainerException {
    public MultiTypeMatchException(Class<?> type) {
        super("There are more than one object of type " + type.getCanonicalName() + " in container.");
    }
}
