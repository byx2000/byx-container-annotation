package byx.ioc.factory.test18;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(AdviceA.class)
public class A {
    @Autowire
    private B b;

    public B getB() {
        return b;
    }

    public int f() {
        return 100;
    }
}
