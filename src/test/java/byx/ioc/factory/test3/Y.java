package byx.ioc.factory.test3;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component @Id("y")
public class Y {
    @Autowired
    @Id("z")
    private Z z;

    public Z getZ() {
        return z;
    }
}
