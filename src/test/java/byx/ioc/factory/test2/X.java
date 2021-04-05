package byx.ioc.factory.test2;

import byx.ioc.annotation.Component;

@Component
public class X {
    private final Y y;

    public X(Y y) {
        Counter.c4++;
        this.y = y;
    }

    public Y getY() {
        return y;
    }
}
