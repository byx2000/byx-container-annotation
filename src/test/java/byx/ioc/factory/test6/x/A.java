package byx.ioc.factory.test6.x;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
public class A {
    private B b;

    public A() {

    }

    @Autowired
    public A(B b) {
        this.b = b;
    }

    public B getB() {
        return b;
    }
}
