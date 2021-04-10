package byx.ioc.factory.test17;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(Advice.class)
public class A {
    @Autowired
    private B b;

    public B getB() {
        return b;
    }

    public int f() {
        return 2002;
    }
}
