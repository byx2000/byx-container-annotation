package byx.ioc.exception;

public class TypeNotFoundException extends ByxContainerException {
    public TypeNotFoundException(Class<?> type) {
        super("There is no object of " + type + " in container.");
    }
}
