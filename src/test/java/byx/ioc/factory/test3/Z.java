package byx.ioc.factory.test3;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component @Id("z")
public class Z {
    @Autowire @Id("x")
    private X x;

    public X getX() {
        return x;
    }
}
