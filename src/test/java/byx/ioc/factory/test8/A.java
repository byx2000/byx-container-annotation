package byx.ioc.factory.test8;

import byx.ioc.annotation.Component;

@Component
public class A {
    private final B b;

    public A(B b) {
        this.b = b;
    }

    public B getB() {
        return b;
    }
}
