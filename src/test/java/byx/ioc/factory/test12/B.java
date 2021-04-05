package byx.ioc.factory.test12;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class B {
    @Autowire
    private A a;

    private final C c;
    private final D d;

    public B(C c, D d) {
        Counter.c2++;
        this.c = c;
        this.d = d;
    }

    public A getA() {
        return a;
    }

    public C getC() {
        return c;
    }

    public D getD() {
        return d;
    }
}
