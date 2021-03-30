package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test4.A;
import byx.ioc.factory.test4.B1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AnnotationContainerFactoryTest4 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test4").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B1 b1 = container.getObject(B1.class);
        assertNotNull(b1);
        assertSame(a.getB(), b1);
    }
}
