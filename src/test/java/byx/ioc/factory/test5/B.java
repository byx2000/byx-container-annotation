package byx.ioc.factory.test5;

import byx.ioc.annotation.Component;

@Component
public class B {
    private final C c;

    public B(C c) {
        this.c = c;
    }

    public C getC() {
        return c;
    }
}
