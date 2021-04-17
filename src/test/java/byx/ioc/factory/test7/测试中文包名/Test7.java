package byx.ioc.factory.test7.测试中文包名;

import byx.ioc.core.Container;
import byx.ioc.factory.AnnotationContainerFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class Test7 {
    @Test
    public void test() {
        Container container = new AnnotationContainerFactory(Test7.class).create();
        for (String id : container.getObjectIds()) {
            System.out.println(id);
        }

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertNotNull(b);
        assertSame(a.getB(), b);
    }
}
