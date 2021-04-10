package byx.ioc.factory.test16;

import byx.aop.annotation.After;
import byx.aop.annotation.Filter;
import byx.ioc.annotation.Component;

@Component
public class Advice {
    @After @Filter(name = "f")
    int after(int retVal) {
        System.out.println("after");
        System.out.println("retVal = " + retVal);
        return retVal + 1;
    }
}
