package byx.ioc.exception;

public class MultiTypeMatchException extends ByxContainerException {
    public MultiTypeMatchException(Class<?> type) {
        super("There more than one object of type " + type.getCanonicalName() + " in container.");
    }
}