package byx.ioc.factory.test6.z;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
public class D {
    @Autowired
    public D() {

    }

    @Autowired
    public D(String s) {

    }
}
