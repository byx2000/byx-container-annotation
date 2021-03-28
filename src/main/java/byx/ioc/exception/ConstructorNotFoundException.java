package byx.ioc.exception;

public class ConstructorNotFoundException extends ByxContainerException {
    public ConstructorNotFoundException(Class<?> type) {
        super("Cannot find properly constructor in class " + type.getCanonicalName());
    }
}
