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
import static byx.ioc.core.ObjectFactory.*;

public class ContainerTest {
    @Test
    public void testNormal() {
        ObjectFactory f1 = of(() -> "hello", String.class);
        ObjectFactory f2 = of(() -> 123, Integer.class);
        ObjectFactory f3 = of(() -> 3.14, Double.class);
        ObjectFactory f4 = of(() -> 6.28, Double.class);

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
    public void testTwoObjectCircularDependency1() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(A::new, obj -> ((A) obj).b = container.getObject(B.class), A.class);
        ObjectFactory f2 = of(B::new, obj -> ((B) obj).a = container.getObject(A.class), B.class);

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
    public void testTwoObjectCircularDependency2() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(A::new, obj -> ((A) obj).b = container.getObject(B.class), A.class);
        ObjectFactory f2 = of(B::new, obj -> ((B) obj).a = container.getObject(A.class), B.class);

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
    public void testTwoObjectCircularDependency3() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(A::new, obj -> ((A) obj).b = container.getObject("b"), A.class);
        ObjectFactory f2 = of(B::new, obj -> ((B) obj).a = container.getObject("a"), B.class);

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
    public void testTwoObjectCircularDependency4() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(A::new, obj -> ((A) obj).b = container.getObject("b"), A.class);
        ObjectFactory f2 = of(B::new, obj -> ((B) obj).a = container.getObject("a"), B.class);

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
    public void testTwoObjectCircularDependency5() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(A::new, obj -> ((A) obj).b = container.getObject("b"), A.class);
        ObjectFactory f2 = of(B::new, obj -> ((B) obj).a = container.getObject(A.class), B.class);

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
    public void testTwoObjectCircularDependency6() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(A::new, obj -> ((A) obj).b = container.getObject(B.class), A.class);
        ObjectFactory f2 = of(B::new, obj -> ((B) obj).a = container.getObject("a"), B.class);

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
            this.d = d;
        }
    }

    public static class D {
        C c;
    }

    @Test
    public void testTwoObjectCircularDependency7() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new C(container.getObject("d")), C.class);
        ObjectFactory f2 = of(D::new, obj -> ((D) obj).c = container.getObject("c"), D.class);

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
    public void testTwoObjectCircularDependency8() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new C(container.getObject("d")), C.class);
        ObjectFactory f2 = of(D::new, obj -> ((D) obj).c = container.getObject("c"), D.class);

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
    public void testTwoObjectCircularDependency9() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new C(container.getObject(D.class)), C.class);
        ObjectFactory f2 = of(D::new, obj -> ((D) obj).c = container.getObject(C.class), D.class);

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
    public void testTwoObjectCircularDependency10() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new C(container.getObject(D.class)), C.class);
        ObjectFactory f2 = of(D::new, obj -> ((D) obj).c = container.getObject(C.class), D.class);

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
    public void testTwoObjectCircularDependency11() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new C(container.getObject(D.class)), C.class);
        ObjectFactory f2 = of(D::new, obj -> ((D) obj).c = container.getObject(C.class), D.class);

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

    @Test
    public void testTwoObjectCircularDependency12() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new C(container.getObject(D.class)), C.class);
        ObjectFactory f2 = of(D::new, obj -> ((D) obj).c = container.getObject("c"), D.class);

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

    @Test
    public void testTwoObjectCircularDependency13() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new C(container.getObject("d")), C.class);
        ObjectFactory f2 = of(D::new, obj -> ((D) obj).c = container.getObject(C.class), D.class);

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
    
    public static class X {
        Y y;
        X() {}
        X(Y y) {
            this.y = y;
        }
    }
    
    public static class Y {
        Z z;
        Y() {}
        Y(Z z) {
            this.z = z;
        }
    }
    
    public static class Z {
        X x;
        Z() {}
        Z(X x) {
            this.x = x;
        }
    }
    
    @Test
    public void testThreeObjectCircularDependency1() {
        Container container = new SimpleContainer();
        
        ObjectFactory f1 = of(X::new, obj -> ((X) obj).y = container.getObject(Y.class), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject(Z.class), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject(X.class), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject(X.class);
        Y y = container.getObject(Y.class);
        Z z = container.getObject(Z.class);

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject("x");
        Y y1 = container.getObject("y");
        Z z1 = container.getObject("z");

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency2() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(X::new, obj -> ((X) obj).y = container.getObject(Y.class), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject(Z.class), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject(X.class), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject("x");
        Y y = container.getObject("y");
        Z z = container.getObject("z");

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject(X.class);
        Y y1 = container.getObject(Y.class);
        Z z1 = container.getObject(Z.class);

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency3() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(X::new, obj -> ((X) obj).y = container.getObject(Y.class), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject("z"), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject(X.class), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject(X.class);
        Y y = container.getObject(Y.class);
        Z z = container.getObject(Z.class);

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject("x");
        Y y1 = container.getObject("y");
        Z z1 = container.getObject("z");

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency4() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new X(container.getObject(Y.class)), X.class);
        ObjectFactory f2 = of(() -> new Y(container.getObject(Z.class)), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject(X.class), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject(X.class);
        Y y = container.getObject(Y.class);
        Z z = container.getObject(Z.class);

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject("x");
        Y y1 = container.getObject("y");
        Z z1 = container.getObject("z");

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency5() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new X(container.getObject(Y.class)), X.class);
        ObjectFactory f2 = of(() -> new Y(container.getObject(Z.class)), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject(X.class), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject("x");
        Y y = container.getObject("y");
        Z z = container.getObject("z");

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject(X.class);
        Y y1 = container.getObject(Y.class);
        Z z1 = container.getObject(Z.class);

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency6() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new X(container.getObject(Y.class)), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject(Z.class), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject(X.class), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject(X.class);
        Y y = container.getObject(Y.class);
        Z z = container.getObject(Z.class);

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject("x");
        Y y1 = container.getObject("y");
        Z z1 = container.getObject("z");

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency7() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new X(container.getObject(Y.class)), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject(Z.class), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject(X.class), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject("x");
        Y y = container.getObject("y");
        Z z = container.getObject("z");

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject(X.class);
        Y y1 = container.getObject(Y.class);
        Z z1 = container.getObject(Z.class);

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency8() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new X(container.getObject(Y.class)), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject(Z.class), Y.class);
        ObjectFactory f3 = of(() -> new Z(container.getObject(X.class)), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject(X.class);
        Y y = container.getObject(Y.class);
        Z z = container.getObject(Z.class);

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject("x");
        Y y1 = container.getObject("y");
        Z z1 = container.getObject("z");

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency9() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(() -> new X(container.getObject(Y.class)), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject(Z.class), Y.class);
        ObjectFactory f3 = of(() -> new Z(container.getObject(X.class)), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject("x");
        Y y = container.getObject("y");
        Z z = container.getObject("z");

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject(X.class);
        Y y1 = container.getObject(Y.class);
        Z z1 = container.getObject(Z.class);

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency10() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(X::new, obj -> ((X) obj).y = container.getObject("y"), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject("z"), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject("x"), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject(X.class);
        Y y = container.getObject(Y.class);
        Z z = container.getObject(Z.class);

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject("x");
        Y y1 = container.getObject("y");
        Z z1 = container.getObject("z");

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }

    @Test
    public void testThreeObjectCircularDependency11() {
        Container container = new SimpleContainer();

        ObjectFactory f1 = of(X::new, obj -> ((X) obj).y = container.getObject("y"), X.class);
        ObjectFactory f2 = of(Y::new, obj -> ((Y) obj).z = container.getObject("z"), Y.class);
        ObjectFactory f3 = of(Z::new, obj -> ((Z) obj).x = container.getObject("x"), Z.class);

        container.registerObject("x", f1);
        container.registerObject("y", f2);
        container.registerObject("z", f3);

        X x = container.getObject("x");
        Y y = container.getObject("y");
        Z z = container.getObject("z");

        assertSame(x.y, y);
        assertSame(y.z, z);
        assertSame(z.x, x);

        X x1 = container.getObject(X.class);
        Y y1 = container.getObject(Y.class);
        Z z1 = container.getObject(Z.class);

        assertSame(x, x1);
        assertSame(y, y1);
        assertSame(z, z1);
    }
}
