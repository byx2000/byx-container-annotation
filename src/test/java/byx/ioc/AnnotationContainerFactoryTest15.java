package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test15.A;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationContainerFactoryTest15 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test15").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        A aa = a.getA();

        assertSame(a, aa);
        assertEquals(889, a.f());
        assertEquals(889, a.f());
    }
}
