package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.Dependency;
import byx.ioc.core.ObjectFactory;
import byx.ioc.core.SimpleContainer;
import byx.ioc.exception.CircularDependencyException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 无法解决的循环依赖（三个对象）
 */
public class SimpleContainerTest17 {
    private static class A {
        B b;
        A(B b) {
            this.b = b;
        }
    }

    private static class B {
        C c;
        B(C c) {
            this.c = c;
        }
    }

    private static class C {
        A a;
        C(A a) {
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
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("b")};
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
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("c")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((C) params[0]);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("a")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        assertThrows(CircularDependencyException.class, () -> container.getObject("a"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("b"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("c"));
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
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(B.class)};
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
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(C.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((C) params[0]);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(A.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        assertThrows(CircularDependencyException.class, () -> container.getObject("a"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("b"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("c"));
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
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(B.class)};
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
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(C.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((C) params[0]);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(A.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        assertThrows(CircularDependencyException.class, () -> container.getObject(A.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(A.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject("b"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("c"));
    }
}
