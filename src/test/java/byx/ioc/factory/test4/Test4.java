package byx.ioc.factory.test4;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class Test4 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test4").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B1 b1 = container.getObject(B1.class);
        assertNotNull(b1);
        assertSame(a.getB(), b1);
    }
}
