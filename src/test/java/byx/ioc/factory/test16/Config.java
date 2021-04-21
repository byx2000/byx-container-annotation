package byx.ioc.factory.test16;

import byx.ioc.annotation.Component;

@Component
public class Config {
    @Component
    public Integer id() {
        return 1001;
    }

    @Component
    public String username() {
        return "byx";
    }

    @Component
    public String password() {
        return "123";
    }
}
