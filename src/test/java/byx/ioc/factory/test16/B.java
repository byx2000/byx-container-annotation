package byx.ioc.factory.test16;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class B {
    @Autowire
    private A a;

    public A getA() {
        return a;
    }
}