package byx.ioc.factory.test18;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.extension.aop.annotation.AdviceBy;

@Component
@AdviceBy(AdviceB.class)
public class B {
    @Autowired
    private A a;

    public A getA() {
        return a;
    }

    public int g() {
        return 200;
    }
}
