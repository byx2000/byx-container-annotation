package byx.ioc.factory.test16;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component
public class C {
    @Autowired
    public void set(@Id("msg") String s) {

    }
}
