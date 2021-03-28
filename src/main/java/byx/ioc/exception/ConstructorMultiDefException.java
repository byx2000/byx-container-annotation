package byx.ioc.exception;

public class ConstructorMultiDefException extends ByxContainerException {
    public ConstructorMultiDefException(Class<?> type) {
        super("There is more than one constructor with @Create annotation in class: " + type.getCanonicalName());
    }
}
