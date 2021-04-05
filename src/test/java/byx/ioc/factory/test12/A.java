package byx.ioc.factory.test12;

import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class A {
    private final B b;
    private final C c;
    private final D d;

    public A(B b, C c, D d) {
        Counter.c1++;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }

    public D getD() {
        return d;
    }
}
