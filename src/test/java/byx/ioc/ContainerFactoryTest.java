package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test1.A;
import byx.ioc.factory.test1.x.B;
import byx.ioc.factory.test2.C;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContainerFactoryTest {
    @Test
    public void test1() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test1").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b1 = container.getObject("b1");
        B b2 = container.getObject("b2");

        assertNotNull(b1);
        assertNotNull(b2);
        assertSame(a.getB(), b1);

        String message = container.getObject("message");
        String info = container.getObject("info");

        assertEquals("hello", message);
        assertEquals("hi", info);

        Integer length = container.getObject(Integer.class);
        assertEquals(message.length(), length);

        Double pi = container.getObject("pi");
        assertEquals(3.14, pi);

        String s2 = container.getObject("s2");
        assertEquals("3.14 hi", s2);
    }

    @Test
    public void test2() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test2").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        byx.ioc.factory.test2.A a = container.getObject(byx.ioc.factory.test2.A.class);
        byx.ioc.factory.test2.B b = container.getObject(byx.ioc.factory.test2.B.class);
        C c = container.getObject(C.class);

        assertSame(a.getB(), b);
        assertSame(b.getA(), a);
        assertSame(c.getC(), c);
    }

    @Test
    public void test3() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test3").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        byx.ioc.factory.test3.A a = container.getObject(byx.ioc.factory.test3.A.class);
        byx.ioc.factory.test3.B b = container.getObject(byx.ioc.factory.test3.B.class);
        byx.ioc.factory.test3.C c = container.getObject(byx.ioc.factory.test3.C.class);

        assertSame(a.getB(), b);
        assertSame(b.getC(), c);
        assertSame(c.getA(), a);
    }
}
