package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test17.A;
import byx.ioc.factory.test17.B;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationContainerFactoryTest17 {
    @Test
    public void test1() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test17").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        A aa = b.getA();
        B bb = a.getB();

        assertSame(a, aa);
        assertSame(b, bb);
        assertEquals(2003, a.f());
        assertEquals(2003, aa.f());
    }

    @Test
    public void test2() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test17").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        B b = container.getObject(B.class);
        A a = container.getObject(A.class);
        A aa = b.getA();
        B bb = a.getB();

        assertSame(a, aa);
        assertSame(b, bb);
        assertEquals(2003, a.f());
        assertEquals(2003, aa.f());
    }
}
