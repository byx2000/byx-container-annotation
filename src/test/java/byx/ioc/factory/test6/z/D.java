package byx.ioc.factory.test6.z;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class D {
    @Autowire
    public D() {

    }

    @Autowire
    public D(String s) {

    }
}
