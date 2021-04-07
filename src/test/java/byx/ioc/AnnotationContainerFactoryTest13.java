package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.exception.CircularDependencyException;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test13.A;
import byx.ioc.factory.test13.B;
import byx.ioc.factory.test13.C;
import byx.ioc.factory.test13.D;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationContainerFactoryTest13 {
    @Test
    public void test1() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test13").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        assertThrows(CircularDependencyException.class, () -> container.getObject(A.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(B.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(C.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(D.class));
    }

    @Test
    public void test2() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test13").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        assertThrows(CircularDependencyException.class, () -> container.getObject(B.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(A.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(C.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(D.class));
    }
}
