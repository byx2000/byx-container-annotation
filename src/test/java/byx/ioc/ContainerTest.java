package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectFactory;
import byx.ioc.core.SimpleContainer;
import byx.ioc.exception.IdDuplicatedException;
import byx.ioc.exception.IdNotFoundException;
import byx.ioc.exception.MultiTypeMatchException;
import byx.ioc.exception.TypeNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {
    @Test
    public void test1() {
        ObjectFactory f1 = ObjectFactory.of(() -> "hello", String.class);
        ObjectFactory f2 = ObjectFactory.of(() -> 123, Integer.class);
        ObjectFactory f3 = ObjectFactory.of(() -> 3.14, Double.class);
        ObjectFactory f4 = ObjectFactory.of(() -> 6.28, Double.class);

        Container container = new SimpleContainer();
        container.registerObject("f1", f1);
        container.registerObject("f2", f2);
        assertThrows(IdDuplicatedException.class, () -> container.registerObject("f1", f1));
        container.registerObject("f3", f3);
        container.registerObject("f4", f4);

        String s = container.getObject(String.class);
        assertEquals("hello", s);

        Integer i = container.getObject(Integer.class);
        assertEquals(123, i);

        String s2 = container.getObject("f1");
        assertSame(s, s2);

        Integer i2 = container.getObject("f2");
        assertSame(i, i2);

        assertThrows(IdNotFoundException.class, () -> container.getObject("f5"));
        assertThrows(TypeNotFoundException.class, () -> container.getObject(Boolean.class));
        assertThrows(MultiTypeMatchException.class, () -> container.getObject(Double.class));
    }

    public static class A {
        B b;
    }

    public static class B {
        A a;
    }

    @Test
    public void test2() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(A::new, obj -> ((A) obj).b = container.getObject(B.class), A.class);
        ObjectFactory f2 = ObjectFactory.of(B::new, obj -> ((B) obj).a = container.getObject(A.class), B.class);

        container.registerObject("a", f1);
        container.registerObject("b", f2);

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertSame(a.b, b);
        assertSame(b.a, a);

        A a1 = container.getObject("a");
        B b1 = container.getObject("b");

        assertSame(a, a1);
        assertSame(b, b1);
    }

    @Test
    public void test3() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(A::new, obj -> ((A) obj).b = container.getObject(B.class), A.class);
        ObjectFactory f2 = ObjectFactory.of(B::new, obj -> ((B) obj).a = container.getObject(A.class), B.class);

        container.registerObject("a", f1);
        container.registerObject("b", f2);

        A a = container.getObject("a");
        B b = container.getObject("b");

        assertSame(a.b, b);
        assertSame(b.a, a);

        A a1 = container.getObject(A.class);
        B b1 = container.getObject(B.class);

        assertSame(a, a1);
        assertSame(b, b1);
    }

    @Test
    public void test4() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(A::new, obj -> ((A) obj).b = container.getObject("b"), A.class);
        ObjectFactory f2 = ObjectFactory.of(B::new, obj -> ((B) obj).a = container.getObject("a"), B.class);

        container.registerObject("a", f1);
        container.registerObject("b", f2);

        A a = container.getObject("a");
        B b = container.getObject("b");

        assertSame(a.b, b);
        assertSame(b.a, a);

        A a1 = container.getObject(A.class);
        B b1 = container.getObject(B.class);

        assertSame(a, a1);
        assertSame(b, b1);
    }

    @Test
    public void test5() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(A::new, obj -> ((A) obj).b = container.getObject("b"), A.class);
        ObjectFactory f2 = ObjectFactory.of(B::new, obj -> ((B) obj).a = container.getObject("a"), B.class);

        container.registerObject("a", f1);
        container.registerObject("b", f2);

        A a = container.getObject(A.class);
        B b = container.getObject(B.class);

        assertSame(a.b, b);
        assertSame(b.a, a);

        A a1 = container.getObject("a");
        B b1 = container.getObject("b");

        assertSame(a, a1);
        assertSame(b, b1);
    }

    public static class C {
        D d;
        C(D d) {
            //System.out.println("lalala");
            this.d = d;
        }
    }

    public static class D {
        C c;
    }

    @Test
    public void test6() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(() -> new C(container.getObject("d")), C.class);
        ObjectFactory f2 = ObjectFactory.of(D::new, obj -> ((D) obj).c = container.getObject("c"), D.class);

        container.registerObject("c", f1);
        container.registerObject("d", f2);

        C c = container.getObject("c");
        D d = container.getObject("d");

        assertSame(c.d, d);
        assertSame(d.c, c);

        C c1 = container.getObject(C.class);
        D d1 = container.getObject(D.class);

        assertSame(c, c1);
        assertSame(d, d1);
    }

    @Test
    public void test7() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(() -> new C(container.getObject("d")), C.class);
        ObjectFactory f2 = ObjectFactory.of(D::new, obj -> ((D) obj).c = container.getObject("c"), D.class);

        container.registerObject("c", f1);
        container.registerObject("d", f2);

        D d = container.getObject("d");
        C c = container.getObject("c");

        assertSame(c.d, d);
        assertSame(d.c, c);

        D d1 = container.getObject(D.class);
        C c1 = container.getObject(C.class);

        assertSame(d, d1);
        assertSame(c, c1);
    }

    @Test
    public void test8() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(() -> new C(container.getObject(D.class)), C.class);
        ObjectFactory f2 = ObjectFactory.of(D::new, obj -> ((D) obj).c = container.getObject(C.class), D.class);

        container.registerObject("c", f1);
        container.registerObject("d", f2);

        C c = container.getObject("c");
        D d = container.getObject("d");

        assertSame(c.d, d);
        assertSame(d.c, c);

        C c1 = container.getObject(C.class);
        D d1 = container.getObject(D.class);

        assertSame(c, c1);
        assertSame(d, d1);
    }

    @Test
    public void test9() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(() -> new C(container.getObject(D.class)), C.class);
        ObjectFactory f2 = ObjectFactory.of(D::new, obj -> ((D) obj).c = container.getObject(C.class), D.class);

        container.registerObject("c", f1);
        container.registerObject("d", f2);

        D d = container.getObject(D.class);
        C c = container.getObject(C.class);

        assertSame(c.d, d);
        assertSame(d.c, c);

        D d1 = container.getObject(D.class);
        C c1 = container.getObject(C.class);

        assertSame(d, d1);
        assertSame(c, c1);
    }

    @Test
    public void test10() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = ObjectFactory.of(() -> new C(container.getObject(D.class)), C.class);
        ObjectFactory f2 = ObjectFactory.of(D::new, obj -> ((D) obj).c = container.getObject(C.class), D.class);

        container.registerObject("c", f1);
        container.registerObject("d", f2);

        C c = container.getObject(C.class);
        D d = container.getObject(D.class);

        assertSame(c.d, d);
        assertSame(d.c, c);

        C c1 = container.getObject("c");
        D d1 = container.getObject("d");

        assertSame(c, c1);
        assertSame(d, d1);
    }
}
