package byx.ioc.factory.test1.x;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component @Id("b1")
public class B1 extends B {
    @Override
    public String toString() {
        return "b1";
    }

    @Component
    public String info() {
        return "hi";
    }
}
