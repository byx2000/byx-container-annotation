package byx.ioc.factory.test4;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component
public class A {
    @Autowire @Id("b1")
    private B b;

    public B getB() {
        return b;
    }
}
