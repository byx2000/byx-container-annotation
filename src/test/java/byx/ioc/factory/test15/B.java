package byx.ioc.factory.test15;

import byx.ioc.annotation.Component;

@Component
public class B {
    @Component
    public Y y(X x) {
        return new Y();
    }
}
