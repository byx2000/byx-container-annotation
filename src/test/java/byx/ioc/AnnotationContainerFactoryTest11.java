package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test11.A;
import byx.ioc.factory.test11.B;
import byx.ioc.factory.test11.C;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationContainerFactoryTest11 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test11").create();
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
