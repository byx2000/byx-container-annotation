package byx.ioc.factory.test13;

import byx.ioc.annotation.Component;

@Component
public class A {
    private final C c;

    public A(C c) {
        this.c = c;
    }

    public C getC() {
        return c;
    }
}
