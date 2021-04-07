package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.Dependency;
import byx.ioc.core.ObjectFactory;
import byx.ioc.core.SimpleContainer;
import byx.ioc.exception.CircularDependencyException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 无法解决的循环依赖（四个对象）
 */
public class SimpleContainerTest20 {
    private static class A {
        A(C c) {

        }
    }

    private static class B {
        B(C c, D d) {

        }
    }

    private static class C {
        C(D d) {

        }
    }

    private static class D {
        D(A a) {

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
                return new Dependency[]{Dependency.id("c")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new A((C) params[0]);
            }
        };

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("c"), Dependency.id("d")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((C) params[0], (D) params[1]);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("d")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((D) params[0]);
            }
        };

        ObjectFactory fd = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return D.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("a")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new D((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);
        container.registerObject("d", fd);

        assertThrows(CircularDependencyException.class, () -> container.getObject("a"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("b"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("c"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("d"));
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
                return new Dependency[]{Dependency.type(C.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new A((C) params[0]);
            }
        };

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(C.class), Dependency.type(D.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((C) params[0], (D) params[1]);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(D.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((D) params[0]);
            }
        };

        ObjectFactory fd = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return D.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.type(A.class)};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new D((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);
        container.registerObject("d", fd);

        assertThrows(CircularDependencyException.class, () -> container.getObject(A.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(B.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(C.class));
        assertThrows(CircularDependencyException.class, () -> container.getObject(D.class));
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
                return new Dependency[]{Dependency.id("c")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new A((C) params[0]);
            }
        };

        ObjectFactory fb = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("c"), Dependency.id("d")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new B((C) params[0], (D) params[1]);
            }
        };

        ObjectFactory fc = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("d")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new C((D) params[0]);
            }
        };

        ObjectFactory fd = new ObjectFactory() {
            @Override
            public Class<?> getType() {
                return D.class;
            }

            @Override
            public Dependency[] getCreateDependencies() {
                return new Dependency[]{Dependency.id("a")};
            }

            @Override
            public Object doCreate(Object[] params) {
                return new D((A) params[0]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);
        container.registerObject("d", fd);

        assertThrows(CircularDependencyException.class, () -> container.getObject("b"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("a"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("c"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("d"));
    }
}
