package byx.ioc.factory.test3;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component @Id("x")
public class X {
    @Autowired
    @Id("y")
    private Y y;

    public Y getY() {
        return y;
    }
}
