package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 一个对象的循环依赖（带AOP代理）
 */
public class SimpleContainerTest7 {
    private static String s = "";

    private static class A {
        private A a;

        public A getA() {
            return a;
        }

        int f() {
            return 100;
        }
    }

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
            public void doInit(Object obj) {
                s += "i";
                ((A) obj).a = container.getObject(A.class);
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

                    @Override
                    public A getA() {
                        return a.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);

        s = "";
        A a = container.getObject(A.class);

        assertSame(a.getA(), a);
        assertEquals(200, a.f());
        assertEquals(200, a.getA().f());
        assertEquals("ciw", s);
    }

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
            public void doInit(Object obj) {
                s += "i";
                ((A) obj).a = container.getObject("a");
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

                    @Override
                    public A getA() {
                        return a.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);

        s = "";
        A a = container.getObject(A.class);

        assertSame(a.getA(), a);
        assertEquals(200, a.f());
        assertEquals(200, a.getA().f());
        assertEquals("ciw", s);
    }
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
            public void doInit(Object obj) {
                s += "i";
                ((A) obj).a = container.getObject("a");
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

                    @Override
                    public A getA() {
                        return a.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);

        s = "";
        A a = container.getObject("a");

        assertSame(a.getA(), a);
        assertEquals(200, a.f());
        assertEquals(200, a.getA().f());
        assertEquals("ciw", s);
    }

    @Test
    public void test4() {
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
            public void doInit(Object obj) {
                s += "i";
                ((A) obj).a = container.getObject("a");
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

                    @Override
                    public A getA() {
                        return a.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);

        s = "";
        A a = container.getObject(A.class);

        assertSame(a.getA(), a);
        assertEquals(200, a.f());
        assertEquals(200, a.getA().f());
        assertEquals("ciw", s);
    }
}
