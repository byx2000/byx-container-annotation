package byx.ioc.factory.test5;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class C {
    @Autowire
    private A a;

    public A getA() {
        return a;
    }
}
