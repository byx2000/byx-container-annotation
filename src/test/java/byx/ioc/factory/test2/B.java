package byx.ioc.factory.test2;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class B {
    @Autowire
    private A a;

    public B() {
        Counter.c2++;
    }

    public A getA() {
        return a;
    }
}
