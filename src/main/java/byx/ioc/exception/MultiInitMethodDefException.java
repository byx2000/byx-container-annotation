package byx.ioc.exception;

/**
 * 找到多个init函数
 *
 * @author byx
 */
public class MultiInitMethodDefException extends ByxContainerException {
    public MultiInitMethodDefException(Class<?> type) {
        super("Multi init method definitions in " + type);
    }
}
