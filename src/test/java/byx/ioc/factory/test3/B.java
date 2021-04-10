package byx.ioc.factory.test3;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
public class B {
    @Autowired
    public C c;

    public C getC() {
        return c;
    }
}
