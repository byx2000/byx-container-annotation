package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test9.A;
import byx.ioc.factory.test9.B;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationContainerFactoryTest9 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test9").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertSame(a.getA(), a);
        assertSame(a.getB(), b);
        assertSame(b.getA(), a);
        assertSame(b.getB(), b);
    }
}
