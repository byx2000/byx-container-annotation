package byx.ioc.factory.test6.x;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Create;

@Component
public class A {
    private B b;

    public A() {

    }

    @Create
    public A(B b) {
        this.b = b;
    }

    public B getB() {
        return b;
    }
}
