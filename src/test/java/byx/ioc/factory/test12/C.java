package byx.ioc.factory.test12;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class C {
    @Autowire
    private A a;

    @Autowire
    private B b;

    private final D d;

    public C(D d) {
        Counter.c3++;
        this.d = d;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public D getD() {
        return d;
    }
}
