package byx.ioc.exception;

/**
 * 找不到ByxAOP依赖
 *
 * @author byx
 */
public class ByxAopNotFoundException extends RuntimeException {
    public ByxAopNotFoundException() {
        super("Cannot found ByxAOP dependency.");
    }
}
