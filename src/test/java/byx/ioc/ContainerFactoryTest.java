package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test1.A;
import byx.ioc.factory.test1.x.B;
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
    }
}
