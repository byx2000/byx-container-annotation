package byx.ioc.factory.test11;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class B {
    @Autowire
    private A a;

    @Autowire
    private C c;

    public A getA() {
        return a;
    }

    public C getC() {
        return c;
    }
}
