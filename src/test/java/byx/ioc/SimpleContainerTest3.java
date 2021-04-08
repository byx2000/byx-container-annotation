package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.Dependency;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 无循环依赖的构造函数注入
 */
public class SimpleContainerTest3 {
    private static class A {
        B b;
        A(B b) {
            this.b = b;
        }
    }

    private static class B {

    }

    /**
     * 根据id注入、根据id获取
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
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        A a = container.getObject("a");
        B b = container.getObject("b");

        assertNotNull(b);
        assertSame(a.b, b);
    }

    /**
     * 根据id注入、根据类型获取
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
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertNotNull(b);
        assertSame(a.b, b);
    }

    /**
     * 根据类型注入、根据id获取
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
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        A a = container.getObject("a");
        B b = container.getObject("b");

        assertNotNull(b);
        assertSame(a.b, b);
    }

    /**
     * 根据类型注入、根据类型获取
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
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertNotNull(b);
        assertSame(a.b, b);
    }

    /**
     * 调换获取顺序
     */
    @Test
    public void test5() {
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
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);

        B b = container.getObject("b");
        A a = container.getObject("a");

        assertNotNull(b);
        assertSame(a.b, b);
    }
}
