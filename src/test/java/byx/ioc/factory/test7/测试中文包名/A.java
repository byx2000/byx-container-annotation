package byx.ioc.factory.test7.测试中文包名;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class A {
    @Autowire
    private B b;

    public B getB() {
        return b;
    }
}
