package byx.ioc.factory.test2;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class A {
    @Autowire
    private B b;

    public A() {
        Counter.c1++;
    }

    public B getB() {
        return b;
    }
}
