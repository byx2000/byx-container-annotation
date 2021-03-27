package byx.ioc.exception;

public class ByxContainerException extends RuntimeException {
    public ByxContainerException(String msg) {
        super(msg);
    }

    public ByxContainerException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
