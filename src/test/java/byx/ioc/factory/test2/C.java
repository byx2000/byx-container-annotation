package byx.ioc.factory.test2;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class C {
    @Autowire
    private C c;

    public C getC() {
        return c;
    }
}
