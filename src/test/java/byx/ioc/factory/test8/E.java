package byx.ioc.factory.test8;

import byx.ioc.annotation.Component;

@Component
public class E {
    private final A a;

    public E(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
