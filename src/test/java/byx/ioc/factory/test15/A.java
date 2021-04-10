package byx.ioc.factory.test15;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(Advice.class)
public class A {
    @Autowired
    private A a;

    public int f() {
        return 888;
    }

    public A getA() {
        return a;
    }
}
