package byx.ioc.factory.test14;

import byx.ioc.annotation.Component;

@Component
public class A {
    @Component
    public int v1() {
        return 123;
    }

    @Component
    public String v2() {
        return "hello";
    }
}
