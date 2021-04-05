package byx.ioc.factory.test12;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class D {
    @Autowire
    private A a;

    @Autowire
    private B b;

    @Autowire
    private C c;

    public D() {
        Counter.c4++;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
}
