package byx.ioc.factory.test8;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
public class D {
    @Autowired
    private E e;

    public E getE() {
        return e;
    }
}
