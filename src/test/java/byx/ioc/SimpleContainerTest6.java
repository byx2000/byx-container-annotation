package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 一个对象的循环依赖
 */
public class SimpleContainerTest6 {
    private static int cnt = 0;

    private static class A {
        A a;
        A() {
            cnt++;
        }
    }

    /**
     * 根据id注入
     */
    @Test
    public void test1() {
        Container container = new SimpleContainer();

        ObjectDefinition fa = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                ((A) obj).a = container.getObject("a");
            }
        };

        container.registerObject("a", fa);

        cnt = 0;
        A a = container.getObject(A.class);

        assertEquals(1, cnt);

        assertSame(a.a, a);
    }

    /**
     * 根据类型注入
     */
    @Test
    public void test2() {
        Container container = new SimpleContainer();

        ObjectDefinition fa = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                ((A) obj).a = container.getObject(A.class);
            }
        };

        container.registerObject("a", fa);

        cnt = 0;
        A a = container.getObject("a");

        assertEquals(1, cnt);

        assertSame(a.a, a);
    }
}
