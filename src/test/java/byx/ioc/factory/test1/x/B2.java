package byx.ioc.factory.test1.x;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Id;

@Component @Id("b2")
public class B2 extends B {
    @Override
    public String toString() {
        return "b2";
    }

    @Component
    public Integer lengthOfMessage(@Id("message") String message) {
        return message.length();
    }
}
