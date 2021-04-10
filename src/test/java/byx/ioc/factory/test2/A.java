package byx.ioc.factory.test2;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class A {
    @Autowired
    private B b;

    public A() {
        Counter.c1++;
    }

    public B getB() {
        return b;
    }
}
