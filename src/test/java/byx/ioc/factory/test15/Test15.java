package byx.ioc.factory.test15;

import byx.ioc.core.Container;
import byx.ioc.exception.CircularDependencyException;
import byx.ioc.factory.AnnotationContainerFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 方法注入构成的循环依赖
 */
public class Test15 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory(Test15.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        assertThrows(CircularDependencyException.class, () -> container.getObject(X.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(Y.class));
    }
}
