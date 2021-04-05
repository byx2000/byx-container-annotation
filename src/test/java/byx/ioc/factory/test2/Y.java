package byx.ioc.factory.test2;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Counter;

@Component
public class Y {
    @Autowire
    private X x;

    public Y() {
        Counter.c5++;
    }

    public X getX() {
        return x;
    }
}
