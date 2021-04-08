package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 两个对象的循环依赖（字段注入、带AOP）
 */
public class SimpleContainerTest10 {
    private static String s1 = "", s2 = "";

    private static class A {
        B b;

        public B getB() {
            return b;
        }

        int f() {
            return 100;
        }
    }

    private static class B {
        A a;

        public A getA() {
            return a;
        }

        int g() {
            return 200;
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
            public Object getInstance(Object[] params) {
                s1 += "c";
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                s1 += "i";
                ((A) obj).b = container.getObject("b");
            }

            @Override
            public Object doWrap(Object obj) {
                s1 += "w";
                A target = (A) obj;
                return new A() {
                    @Override
                    int f() {
                        return 1 + target.f();
                    }

                    @Override
                    public B getB() {
                        return target.getB();
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
                s2 += "c";
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                s2 += "i";
                ((B) obj).a = container.getObject("a");
            }

            @Override
            public Object doWrap(Object obj) {
                s2 += "w";
                B target = (B) obj;
                return new B() {
                    @Override
                    int g() {
                        return 2 + target.g();
                    }

                    @Override
                    public A getA() {
                        return target.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        s1 = s2 = "";
        A a = container.getObject("a");
        B b = container.getObject("b");
        B bb = a.getB();
        A aa = b.getA();

        assertEquals("ciw", s1);
        assertEquals("ciw", s2);

        assertSame(bb, b);
        assertSame(aa, a);
        assertEquals(101, a.f());
        assertEquals(202, b.g());
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
            public Object getInstance(Object[] params) {
                s1 += "c";
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                s1 += "i";
                ((A) obj).b = container.getObject(B.class);
            }

            @Override
            public Object doWrap(Object obj) {
                s1 += "w";
                A target = (A) obj;
                return new A() {
                    @Override
                    int f() {
                        return 1 + target.f();
                    }

                    @Override
                    public B getB() {
                        return target.getB();
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
                s2 += "c";
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                s2 += "i";
                ((B) obj).a = container.getObject(A.class);
            }

            @Override
            public Object doWrap(Object obj) {
                s2 += "w";
                B target = (B) obj;
                return new B() {
                    @Override
                    int g() {
                        return 2 + target.g();
                    }

                    @Override
                    public A getA() {
                        return target.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        s1 = s2 = "";
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        B bb = a.getB();
        A aa = b.getA();

        assertEquals("ciw", s1);
        assertEquals("ciw", s2);

        assertSame(bb, b);
        assertSame(aa, a);
        assertEquals(101, a.f());
        assertEquals(202, b.g());
    }

    /**
     * A根据id注入，B根据类型注入
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
                s1 += "c";
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                s1 += "i";
                ((A) obj).b = container.getObject(B.class);
            }

            @Override
            public Object doWrap(Object obj) {
                s1 += "w";
                A target = (A) obj;
                return new A() {
                    @Override
                    int f() {
                        return 1 + target.f();
                    }

                    @Override
                    public B getB() {
                        return target.getB();
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
                s2 += "c";
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                s2 += "i";
                ((B) obj).a = container.getObject("a");
            }

            @Override
            public Object doWrap(Object obj) {
                s2 += "w";
                B target = (B) obj;
                return new B() {
                    @Override
                    int g() {
                        return 2 + target.g();
                    }

                    @Override
                    public A getA() {
                        return target.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        s1 = s2 = "";
        A a = container.getObject("a");
        B b = container.getObject("b");
        B bb = a.getB();
        A aa = b.getA();

        assertEquals("ciw", s1);
        assertEquals("ciw", s2);

        assertSame(bb, b);
        assertSame(aa, a);
        assertEquals(101, a.f());
        assertEquals(202, b.g());
    }

    /**
     * A根据类型注入，B根据id注入
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
            public Object getInstance(Object[] params) {
                s1 += "c";
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                s1 += "i";
                ((A) obj).b = container.getObject("b");
            }

            @Override
            public Object doWrap(Object obj) {
                s1 += "w";
                A target = (A) obj;
                return new A() {
                    @Override
                    int f() {
                        return 1 + target.f();
                    }

                    @Override
                    public B getB() {
                        return target.getB();
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
                s2 += "c";
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                s2 += "i";
                ((B) obj).a = container.getObject(A.class);
            }

            @Override
            public Object doWrap(Object obj) {
                s2 += "w";
                B target = (B) obj;
                return new B() {
                    @Override
                    int g() {
                        return 2 + target.g();
                    }

                    @Override
                    public A getA() {
                        return target.getA();
                    }
                };
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        s1 = s2 = "";
        A a = container.getObject("a");
        B b = container.getObject("b");
        B bb = a.getB();
        A aa = b.getA();

        assertEquals("ciw", s1);
        assertEquals("ciw", s2);

        assertSame(bb, b);
        assertSame(aa, a);
        assertEquals(101, a.f());
        assertEquals(202, b.g());
    }
}
