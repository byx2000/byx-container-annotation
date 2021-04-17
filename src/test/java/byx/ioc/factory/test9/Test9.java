package byx.ioc.factory.test9;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class Test9 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory(Test9.class).create();
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
