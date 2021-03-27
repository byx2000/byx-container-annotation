package byx.ioc.factory.test1;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;
import byx.ioc.factory.test1.x.B;

@Component
public class A {
    private final B b;

    public A(@Id("b1") B b) {
        this.b = b;
    }

    public B getB() {
        return b;
    }

    @Component
    public String message() {
        return "hello";
    }
}
