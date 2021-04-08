package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 三个对象的循环依赖（字段注入，无AOP）
 */
public class SimpleContainerTest12 {
    private static class A {
        B b;
    }

    private static class B {
        C c;
    }

    private static class C {
        A a;
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
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                ((A) obj).b = container.getObject("b");
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
                ((B) obj).c = container.getObject("c");
            }
        };

        ObjectDefinition fc = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new C();
            }

            @Override
            public void doInit(Object obj) {
                ((C) obj).a = container.getObject("a");
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);

        assertSame(a.b, b);
        assertSame(b.c, c);
        assertSame(c.a, a);
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
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                ((A) obj).b = container.getObject(B.class);
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
                ((B) obj).c = container.getObject(C.class);
            }
        };

        ObjectDefinition fc = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new C();
            }

            @Override
            public void doInit(Object obj) {
                ((C) obj).a = container.getObject(A.class);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);

        assertSame(a.b, b);
        assertSame(b.c, c);
        assertSame(c.a, a);
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
                return new A();
            }

            @Override
            public void doInit(Object obj) {
                ((A) obj).b = container.getObject(B.class);
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
                ((B) obj).c = container.getObject("c");
            }
        };

        ObjectDefinition fc = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return C.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return new C();
            }

            @Override
            public void doInit(Object obj) {
                ((C) obj).a = container.getObject(A.class);
            }
        };

        container.registerObject("a", fa);
        container.registerObject("b", fb);
        container.registerObject("c", fc);

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);
        C c = container.getObject(C.class);

        assertSame(a.b, b);
        assertSame(b.c, c);
        assertSame(c.a, a);
    }
}
