package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.exception.ConstructorMultiDefException;
import byx.ioc.exception.ConstructorNotFoundException;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test6.x.A;
import byx.ioc.factory.test6.x.B;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnnotationContainerFactoryTest6 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test6.x").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertNotNull(b);
        assertSame(a.getB(), b);

        assertThrows(ConstructorNotFoundException.class, () -> new AnnotationContainerFactory("byx.ioc.factory.test6.y").create());
        assertThrows(ConstructorMultiDefException.class, () -> new AnnotationContainerFactory("byx.ioc.factory.test6.z").create());
    }
}
