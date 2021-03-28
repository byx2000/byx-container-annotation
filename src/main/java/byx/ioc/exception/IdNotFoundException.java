package byx.ioc.exception;

/**
 * 找不到指定id的对象
 *
 * @author byx
 */
public class IdNotFoundException extends ByxContainerException {
    public IdNotFoundException(String id) {
        super("There is no object with id \"" + id + "\" in container.");
    }
}
