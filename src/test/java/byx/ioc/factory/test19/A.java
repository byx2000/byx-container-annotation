package byx.ioc.factory.test19;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(AdviceA.class)
public class A {
    private B b;

    public A() {}

    @Autowired
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
