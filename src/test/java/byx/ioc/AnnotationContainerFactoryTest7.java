package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import byx.ioc.factory.test7.测试中文包名.A;
import byx.ioc.factory.test7.测试中文包名.B;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class AnnotationContainerFactoryTest7 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory("byx.ioc.factory.test7.测试中文包名").create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertNotNull(b);
        assertSame(a.getB(), b);
    }
}
