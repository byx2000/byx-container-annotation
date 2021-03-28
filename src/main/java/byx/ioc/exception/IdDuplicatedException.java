package byx.ioc.exception;

/**
 * 使用一个已存在的id注册对象
 *
 * @author byx
 */
public class IdDuplicatedException extends ByxContainerException {
    public IdDuplicatedException(String id) {
        super("Id has exist: " + id);
    }
}
