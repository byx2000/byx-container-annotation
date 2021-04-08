package byx.ioc.factory.test19;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(AdviceB.class)
public class B {
    @Autowire
    private A a;

    public A getA() {
        return a;
    }

    public int g() {
        return 200;
    }
}
