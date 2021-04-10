package byx.ioc.factory.test15;

import byx.aop.annotation.After;
import byx.aop.annotation.Filter;
import byx.ioc.annotation.Component;

@Component
public class Advice {
    @After @Filter(name = "f")
    public int after(int retVal) {
        System.out.println("after");
        System.out.println("retVal = " + retVal);
        return retVal + 1;
    }
}
