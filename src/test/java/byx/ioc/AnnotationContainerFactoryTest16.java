package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test16.A;
import byx.ioc.factory.test16.B;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationContainerFactoryTest16 {
    @Test
    public void test1() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test16").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        A aa = b.getA();

        assertSame(a, aa);
        assertEquals(1002, a.f());
        assertEquals(1002, aa.f());
    }

    @Test
    public void test2() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test16").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        B b = container.getObject(B.class);
        A aa = b.getA();
        A a = container.getObject(A.class);

        assertSame(a, aa);
        assertEquals(1002, a.f());
        assertEquals(1002, aa.f());
    }
}
