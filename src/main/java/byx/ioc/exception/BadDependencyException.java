package byx.ioc.exception;

import byx.ioc.core.Dependency;

/**
 * 不合法的依赖项
 *
 * @author byx
 */
public class BadDependencyException extends ByxContainerException {
    public BadDependencyException(Dependency dependency) {
        super("Bad dependency: " + dependency);
    }
}
