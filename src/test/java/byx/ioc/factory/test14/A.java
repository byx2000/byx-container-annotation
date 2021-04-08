package byx.ioc.factory.test14;

import byx.ioc.annotation.AdviceBy;
import byx.ioc.annotation.Component;
import static org.junit.jupiter.api.Assertions.*;

@Component
@AdviceBy(Advice.class)
public class A {
    public void f(int n) {
        assertEquals(124, n);
        System.out.println("f");
        System.out.println("n = " + n);
    }

    public void g() {
        System.out.println("g");
    }
}
