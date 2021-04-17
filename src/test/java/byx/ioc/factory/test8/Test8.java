package byx.ioc.factory.test8;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class Test8 {
    @Test
    public void test8() {
        Container container = new AnnotationContainerFactory(Test8.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);
        D d = container.getObject(D.class);
        E e = container.getObject(E.class);

        assertSame(a.getB(), b);
        assertSame(b.getC(), c);
        assertSame(c.getD(), d);
        assertSame(d.getE(), e);
        assertSame(e.getA(), a);
    }
}
