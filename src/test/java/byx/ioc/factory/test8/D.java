package byx.ioc.factory.test8;

import byx.ioc.annotation.Autowire;
import byx.ioc.annotation.Component;

@Component
public class D {
    @Autowire
    private E e;

    public E getE() {
        return e;
    }
}
