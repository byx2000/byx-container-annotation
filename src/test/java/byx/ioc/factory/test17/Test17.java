package byx.ioc.factory.test17;

import byx.ioc.core.Container;
import byx.ioc.exception.MultiInitMethodDefException;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.State;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 使用Init注解指定初始化方法
 */
public class Test17 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory(Test17.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        State.state = "";
        A a = container.getObject(A.class);
        assertEquals("cssi", State.state);

        State.state = "";
        B b = container.getObject(B.class);
        assertEquals("hello", State.state);

        assertThrows(MultiInitMethodDefException.class, () -> container.getObject(C.class));
    }
}
