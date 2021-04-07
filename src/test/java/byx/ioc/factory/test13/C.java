package byx.ioc.factory.test13;

import byx.ioc.annotation.Component;

@Component
public class C {
    private final D d;

    public C(D d) {
        this.d = d;
    }

    public D getD() {
        return d;
    }
}
