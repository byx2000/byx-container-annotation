package byx.ioc.exception;

import java.util.List;

public class CircularDependencyException extends ByxContainerException {
    public CircularDependencyException(List<String> ids) {
        super("An unresolvable circular dependency was detected: " + ids);
    }
}
