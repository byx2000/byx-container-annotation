package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectFactory;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 带AOP的构造函数注入（无循环依赖）
 */
public class SimpleContainerTest9 {
    private static String s = "";
    
    private static class A {
        int f() {
            return 100;
        }
    }

    private static class B {
        private final A a;

        private B(A a) {
            this.a = a;
        }

        public A getA() {
            return a;
        }
    }

    /**
     * 根据类型注入
     */
    @Test
    public void test1() {
        Container container = new SimpleContainer();

        ObjectFactory fa = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object doCreate(Object[] params) {
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

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject(A.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        s = "";
        A a = container.getObject("a");
        B b = container.getObject("b");

        assertEquals("cw", s);
        assertSame(b.a, a);
        assertEquals(200, a.f());
        assertEquals(200, b.a.f());
    }

    /**
     * 根据id注入
     */
    @Test
    public void test2() {
        Container container = new SimpleContainer();

        ObjectFactory fa = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object doCreate(Object[] params) {
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

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject("a")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        s = "";
        A a = container.getObject("a");
        B b = container.getObject("b");

        assertEquals("cw", s);
        assertSame(b.a, a);
        assertEquals(200, a.f());
        assertEquals(200, b.a.f());
    }
}
