package byx.ioc.factory.test3;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class B {
    @Autowire
    private C c;

    public C getC() {
        return c;
    }
}
