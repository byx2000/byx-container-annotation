package byx.ioc.factory.test9;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class B {
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
