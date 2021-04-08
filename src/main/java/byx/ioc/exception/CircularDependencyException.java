package byx.ioc.exception;

import java.util.List;

/**
 * 检测到无法解决的循环依赖
 *
 * @author byx
 */
public class CircularDependencyException extends ByxContainerException {
    public CircularDependencyException(List<String> ids) {
        super("An unresolvable circular dependency was detected: " + ids);
    }
}
