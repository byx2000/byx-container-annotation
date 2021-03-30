package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test2.A;
import byx.ioc.factory.test2.B;
import byx.ioc.factory.test2.C;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class AnnotationContainerFactoryTest2 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test2").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);

        assertSame(a.getB(), b);
        assertSame(b.getA(), a);
        assertSame(c.getC(), c);
    }
}
