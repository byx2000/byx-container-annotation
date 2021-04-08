package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.Dependency;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import byx.ioc.exception.CircularDependencyException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 无法解决的循环依赖（一个对象）
 */
public class SimpleContainerTest15 {
    private static class A {
        A a;
        A(A a) {
            this.a = a;
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
                return new Dependency[]{Dependency.id("a")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((A) params[0]);
            }
        };

        container.registerObject("a", fa);

        assertThrows(CircularDependencyException.class, () -> container.getObject("a"));
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
                return new Dependency[]{Dependency.type(A.class)};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((A) params[0]);
            }
        };

        container.registerObject("a", fa);

        assertThrows(CircularDependencyException.class, () -> container.getObject("a"));
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
            public Dependency[] getInstanceDependencies() {
                return new Dependency[]{Dependency.id("a")};
            }

            @Override
            public Object getInstance(Object[] params) {
                return new A((A) params[0]);
            }
        };

        container.registerObject("a", fa);

        assertThrows(CircularDependencyException.class, () -> container.getObject(A.class));
    }
}
