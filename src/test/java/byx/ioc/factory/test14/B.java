package byx.ioc.factory.test14;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component
public class B {
    @Component
    public String v3(@Id("v2") String s, int i) {
        return s + " " + i;
    }
}
