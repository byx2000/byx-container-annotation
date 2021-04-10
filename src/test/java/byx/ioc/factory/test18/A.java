package byx.ioc.factory.test18;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(AdviceA.class)
public class A {
    @Autowired
    private B b;

    public B getB() {
        return b;
    }

    public int f() {
        return 100;
    }
}
