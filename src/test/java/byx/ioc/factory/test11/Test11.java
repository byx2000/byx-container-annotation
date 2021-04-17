package byx.ioc.factory.test11;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class Test11 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory(Test11.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);

        assertSame(a.getB(), b);
        assertSame(a.getC(), c);
        assertSame(b.getA(), a);
        assertSame(b.getC(), c);
        assertSame(c.getA(), a);
        assertSame(c.getB(), b);
    }
}
