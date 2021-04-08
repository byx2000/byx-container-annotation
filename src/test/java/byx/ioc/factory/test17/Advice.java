package byx.ioc.factory.test17;

import byx.aop.annotation.After;
import byx.aop.annotation.WithName;
import byx.ioc.annotation.Component;

@Component
public class Advice {
    @After @WithName("f")
    public int after(int retVal) {
        System.out.println("after");
        System.out.println("retVal = " + retVal);
        return retVal + 1;
    }
}
