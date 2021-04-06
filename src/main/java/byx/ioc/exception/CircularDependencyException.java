package byx.ioc.exception;

public class CircularDependencyException extends ByxContainerException {
    public CircularDependencyException() {
        super("An unresolvable circular dependency was detected.");
    }
}
