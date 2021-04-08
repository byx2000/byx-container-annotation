package byx.ioc.factory.test18;

import byx.aop.annotation.After;
import byx.aop.annotation.WithName;
import byx.ioc.annotation.Component;

@Component
public class AdviceB {
    @After @WithName("g")
    public int after(int retVal) {
        return retVal + 2;
    }
}
