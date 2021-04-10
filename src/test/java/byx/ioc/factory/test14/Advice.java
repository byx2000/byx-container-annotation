package byx.ioc.factory.test14;

import byx.aop.annotation.After;
import byx.aop.annotation.Before;
import byx.aop.annotation.Filter;
import byx.ioc.annotation.Component;
import byx.ioc.factory.Flag;
import static org.junit.jupiter.api.Assertions.*;

@Component
public class Advice {
    @Before @Filter(name = "f")
    public int[] before(int n) {
        Flag.f1 = true;
        assertEquals(123, n);
        System.out.println("before");
        System.out.println("n = " + n);
        return new int[]{n + 1};
    }

    @After
    public void after() {
        Flag.f2 = true;
        System.out.println("after");
    }
}
