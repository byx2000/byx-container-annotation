package byx.ioc.factory.test15;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(Advice.class)
public class A {
    @Autowire
    private A a;

    public int f() {
        return 888;
    }

    public A getA() {
        return a;
    }
}
