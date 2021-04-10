package byx.ioc.factory.test2;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class C {
    @Autowired
    private C c;

    public C() {
        Counter.c3++;
    }

    public C getC() {
        return c;
    }
}
