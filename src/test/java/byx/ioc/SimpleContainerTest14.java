package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectFactory;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 三个对象的循环依赖（1个字段注入、2个构造函数注入，无AOP）
 */
public class SimpleContainerTest14 {
    private static int c1 = 0, c2 = 0, c3 = 0;

    private static class A {
        B b;
        A(B b) {
            c1++;
            this.b = b;
        }
    }

    private static class B {
        C c;
        B() {
            c2++;
        }
    }

    private static class C {
        A a;
        C(A a) {
            c3++;
            this.a = a;
        }
    }

    @Test
    public void test1() {
        Container container = new SimpleContainer();

        ObjectFactory fa = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject("b")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).c = container.getObject("c");
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject("a")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        c1 = c2 = c3 = 0;
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);

        assertEquals(1, c1);
        assertEquals(1, c2);
        assertEquals(1, c3);

        assertEquals(a.b, b);
        assertEquals(b.c, c);
        assertEquals(c.a, a);
    }

    @Test
    public void test2() {
        Container container = new SimpleContainer();

        ObjectFactory fa = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject("b")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).c = container.getObject("c");
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject("a")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        c1 = c2 = c3 = 0;
        B b = container.getObject(B.class);
        A a = container.getObject(A.class);
        C c = container.getObject(C.class);

        assertEquals(1, c1);
        assertEquals(1, c2);
        assertEquals(1, c3);

        assertEquals(a.b, b);
        assertEquals(b.c, c);
        assertEquals(c.a, a);
    }

    @Test
    public void test3() {
        Container container = new SimpleContainer();

        ObjectFactory fa = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject(B.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).c = container.getObject(C.class);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject(A.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        c1 = c2 = c3 = 0;
        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);

        assertEquals(1, c1);
        assertEquals(1, c2);
        assertEquals(1, c3);

        assertEquals(a.b, b);
        assertEquals(b.c, c);
        assertEquals(c.a, a);
    }

    @Test
    public void test4() {
        Container container = new SimpleContainer();

        ObjectFactory fa = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject(B.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new A((B) params[0]);
            }
        };

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B();
            }

            @Override
            public void doInit(Object obj) {
                ((B) obj).c = container.getObject(C.class);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Object[] getCreateDependencies() {
                return new Object[]{container.getObject("a")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        c1 = c2 = c3 = 0;
        A a = container.getObject("a");
        B b = container.getObject("b");
        C c = container.getObject("c");

        assertEquals(1, c1);
        assertEquals(1, c2);
        assertEquals(1, c3);

        assertEquals(a.b, b);
        assertEquals(b.c, c);
        assertEquals(c.a, a);
    }
}
