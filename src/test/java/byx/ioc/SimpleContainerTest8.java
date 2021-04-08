package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 带AOP的字段注入（无循环依赖）
 */
public class SimpleContainerTest8 {
    private static String s = "";

    private static class A {
        int f() {
            return 100;
        }
    }

    private static class B {
        A a;
    }

    /**
     * 根据类型注入
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
                s += "c";
                return new A();
            }

            @Override
            public Object doWrap(Object obj) {
                s += "w";
                A a = (A) obj;
                return new A() {
                    @Override
                    int f() {
                        return 100 + a.f();
                    }
                };
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

        s = "";
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertEquals("cw", s);

        assertSame(a, b.a);
        assertEquals(200, a.f());
        assertEquals(200, b.a.f());
    }

    /**
     * 根据id注入
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
                s += "c";
                return new A();
            }

            @Override
            public Object doWrap(Object obj) {
                s += "w";
                A a = (A) obj;
                return new A() {
                    @Override
                    int f() {
                        return 100 + a.f();
                    }
                };
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

        s = "";
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertEquals("cw", s);

        assertSame(a, b.a);
        assertEquals(200, a.f());
        assertEquals(200, b.a.f());
    }

    /**
     * 调换获取顺序
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
            public Object getInstance(Object[] params) {
                s += "c";
                return new A();
            }

            @Override
            public Object doWrap(Object obj) {
                s += "w";
                A a = (A) obj;
                return new A() {
                    @Override
                    int f() {
                        return 100 + a.f();
                    }
                };
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

        s = "";
        B b = container.getObject("b");
        A a = container.getObject("a");

        assertEquals("cw", s);

        assertSame(a, b.a);
        assertEquals(200, a.f());
        assertEquals(200, b.a.f());
    }
}
