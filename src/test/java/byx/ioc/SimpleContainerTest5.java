package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.Dependency;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 两个对象的循环依赖（字段注入和构造函数注入）
 */
public class SimpleContainerTest5 {
    private static int cnt1 = 0, cnt2 = 0;

    private static class A {
        B b;
        A(B b) {
            cnt1++;
            this.b = b;
        }
    }

    private static class B {
        A a;
        B() {
            cnt2++;
        }
    }

    /**
     * A和B都根据id注入
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
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("b")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectDefinition fb = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).a = container.getObject("a");
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        cnt1 = cnt2 = 0;
        A a = container.getObject(A.class);
        B b = container.getObject("b");

        assertEquals(1, cnt1);
        assertEquals(1, cnt2);

        assertSame(a.b, b);
        assertSame(b.a, a);
    }

    /**
     * A和B都根据类型注入
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
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.type(B.class)};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectDefinition fb = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).a = container.getObject(A.class);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        cnt1 = cnt2 = 0;
        A a = container.getObject("a");
        B b = container.getObject("b");

        assertEquals(1, cnt1);
        assertEquals(1, cnt2);

        assertSame(a.b, b);
        assertSame(b.a, a);
    }

    /**
     * A根据类型注入，B根据id注入
     */
    @Test
    public void test3() {
        Container container = new SimpleContainer();

        ObjectDefinition fa = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("b")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectDefinition fb = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).a = container.getObject(A.class);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        cnt1 = cnt2 = 0;
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertEquals(1, cnt1);
        assertEquals(1, cnt2);

        assertSame(a.b, b);
        assertSame(b.a, a);
    }

    /**
     * 交换获取顺序
     */
    @Test
    public void test4() {
        Container container = new SimpleContainer();

        ObjectDefinition fa = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("b")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectDefinition fb = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).a = container.getObject("a");
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        cnt1 = cnt2 = 0;
        B b = container.getObject("b");
        A a = container.getObject(A.class);

        assertEquals(1, cnt1);
        assertEquals(1, cnt2);

        assertSame(a.b, b);
        assertSame(b.a, a);
    }
}
