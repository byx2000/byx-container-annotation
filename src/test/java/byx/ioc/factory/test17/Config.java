package byx.ioc.factory.test17;

import byx.ioc.annotation.Component;

@Component
public class Config {
    @Component
    public String msg() {
        return "hello";
    }

    @Component
    public Integer val() {
        return 123;
    }
}
