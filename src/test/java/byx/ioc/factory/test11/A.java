package byx.ioc.factory.test11;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class A {
    @Autowire
    private B b;

    @Autowire
    private C c;

    public B getB() {
        return b;
    }

    public C getC() {
        return c;
    }
}
