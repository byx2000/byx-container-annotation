package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.Flag;
import byx.ioc.factory.test14.A;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnnotationContainerFactoryTest14 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test14").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);

        Flag.f1 = Flag.f2 = false;
        a.f(123);
        assertTrue(Flag.f1);
        assertTrue(Flag.f2);

        Flag.f1 = Flag.f2 = false;
        a.g();
        assertFalse(Flag.f1);
        assertTrue(Flag.f2);
    }
}
