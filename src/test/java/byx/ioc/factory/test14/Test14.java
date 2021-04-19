package byx.ioc.factory.test14;

import byx.ioc.core.Container;
import byx.ioc.exception.MultiTypeMatchException;
import byx.ioc.factory.AnnotationContainerFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 实例方法组件方法参数注入
 */
public class Test14 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory(Test14.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        assertEquals(123, container.getObject(int.class));
        assertThrows(MultiTypeMatchException.class, () -> container.getObject(String.class));
        assertEquals("hello", container.getObject("v2"));
        assertEquals("hello 123", container.getObject("v3"));

        assertThrows(MultiTypeMatchException.class, () -> container.getObject(C.class));
        assertEquals(12345, ((C) container.getObject("cc")).getVal());
    }
}
