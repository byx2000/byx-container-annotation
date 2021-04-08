package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.Dependency;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import byx.ioc.exception.CircularDependencyException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 无法解决的循环依赖（三个对象）
 */
public class SimpleContainerTest19 {
    private static class A {
        A(B b, C c) {

        }
    }

    private static class B {
        B(C c) {

        }
    }

    private static class C {
        C(A a, B b) {

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
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("b"), Dependency.id("c")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((B) params[0], (C) params[1]);
            }
        };

        ObjectDefinition fb = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("c")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new B((C) params[0]);
            }
        };

        ObjectDefinition fc = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("a"), Dependency.id("b")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new C((A) params[0], (B) params[1]);
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

        ObjectDefinition fa = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return A.class;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.type(B.class), Dependency.id("c")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((B) params[0], (C) params[1]);
            }
        };

        ObjectDefinition fb = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return B.class;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("c")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new B((C) params[0]);
            }
        };

        ObjectDefinition fc = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.type(A.class), Dependency.type(B.class)};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new C((A) params[0], (B) params[1]);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        assertThrows(CircularDependencyException.class, () -> container.getObject("b"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("a"));
        assertThrows(CircularDependencyException.class, () -> container.getObject("c"));
    }
}
