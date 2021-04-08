package byx.ioc.factory.test16;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Component;

@Component
@AdviceBy(Advice.class)
public class A {
    public int f() {
        return 1001;
    }
}
