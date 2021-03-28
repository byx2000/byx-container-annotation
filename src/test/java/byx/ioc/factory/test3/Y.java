package byx.ioc.factory.test3;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component @Id("y")
public class Y {
    @Autowire @Id("z")
    private Z z;

    public Z getZ() {
        return z;
    }
}
