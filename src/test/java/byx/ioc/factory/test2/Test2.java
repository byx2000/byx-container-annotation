package byx.ioc.factory.test2;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.Counter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class Test2 {
    @Test
    public void test1() {
        Container container = new AnnotationContainerFactory(Test2.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        Counter.c1 = Counter.c2 = Counter.c3 = Counter.c4 = Counter.c5 = 0;
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);
        X x = container.getObject(X.class);
        Y y = container.getObject(Y.class);

        assertEquals(1, Counter.c1);
        assertEquals(1, Counter.c2);
        assertEquals(1, Counter.c3);
        assertEquals(1, Counter.c4);
        assertEquals(1, Counter.c5);

        assertSame(a.getB(), b);
        assertSame(b.getA(), a);
        assertSame(c.getC(), c);
        assertSame(x.getY(), y);
        assertSame(y.getX(), x);
    }

    @Test
    public void test2() {
        Container container = new AnnotationContainerFactory(Test2.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        Counter.c1 = Counter.c2 = Counter.c3 = Counter.c4 = Counter.c5 = 0;

        C c = container.getObject(C.class);
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        Y y = container.getObject(Y.class);
        X x = container.getObject(X.class);

        assertEquals(1, Counter.c1);
        assertEquals(1, Counter.c2);
        assertEquals(1, Counter.c3);
        assertEquals(1, Counter.c4);
        assertEquals(1, Counter.c5);

        assertSame(a.getB(), b);
        assertSame(b.getA(), a);
        assertSame(c.getC(), c);
        assertSame(x.getY(), y);
        assertSame(y.getX(), x);
    }
}
