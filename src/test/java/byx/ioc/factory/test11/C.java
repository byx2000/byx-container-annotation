package byx.ioc.factory.test11;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class C {
    @Autowire
    private A a;

    @Autowire
    private B b;

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }
}
