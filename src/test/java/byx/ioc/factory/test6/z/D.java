package byx.ioc.factory.test6.z;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Create;

@Component
public class D {
    @Create
    public D() {

    }

    @Create
    public D(String s) {

    }
}
