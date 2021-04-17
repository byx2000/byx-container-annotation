package byx.ioc.factory.test12;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.Counter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class Test12 {
    @Test
    public void test1() {
        Container container = new AnnotationContainerFactory(Test12.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        Counter.c1 = Counter.c2 = Counter.c3 = Counter.c4 = 0;
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);
        D d = container.getObject(D.class);

        assertEquals(1, Counter.c1);
        assertEquals(1, Counter.c2);
        assertEquals(1, Counter.c3);
        assertEquals(1, Counter.c4);

        assertSame(a.getB(), b);
        assertSame(a.getC(), c);
        assertSame(a.getD(), d);
        assertSame(b.getA(), a);
        assertSame(b.getC(), c);
        assertSame(b.getD(), d);
        assertSame(c.getA(), a);
        assertSame(c.getB(), b);
        assertSame(c.getD(), d);
        assertSame(d.getA(), a);
        assertSame(d.getB(), b);
        assertSame(d.getC(), c);
    }

    @Test
    public void test2() {
        Container container = new AnnotationContainerFactory(Test12.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        Counter.c1 = Counter.c2 = Counter.c3 = Counter.c4 = 0;
        C c = container.getObject(C.class);
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        D d = container.getObject(D.class);

        assertEquals(1, Counter.c1);
        assertEquals(1, Counter.c2);
        assertEquals(1, Counter.c3);
        assertEquals(1, Counter.c4);

        assertSame(a.getB(), b);
        assertSame(a.getC(), c);
        assertSame(a.getD(), d);
        assertSame(b.getA(), a);
        assertSame(b.getC(), c);
        assertSame(b.getD(), d);
        assertSame(c.getA(), a);
        assertSame(c.getB(), b);
        assertSame(c.getD(), d);
        assertSame(d.getA(), a);
        assertSame(d.getB(), b);
        assertSame(d.getC(), c);
    }
}
