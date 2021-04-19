package byx.ioc.factory.test15;

import byx.ioc.annotation.Component;

@Component
public class A {
    @Component
    public X x(Y y) {
        return new X();
    }
}
