package byx.ioc.factory.test19;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(AdviceA.class)
public class A {
    private B b;

    public A() {}

    @Autowire
    public A(B b) {
        this.b = b;
    }

    public B getB() {
        return b;
    }

    public int f() {
        return 100;
    }
}
