package byx.ioc.factory.test8;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
public class B {
    @Autowired
    private C c;

    public C getC() {
        return c;
    }
}
