package byx.ioc.factory.test17;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;
import byx.ioc.annotation.Init;
import byx.ioc.factory.State;

@Component
public class A {
    public A() {
        System.out.println("constructor");
        State.state += "c";
    }

    @Autowired
    public void set1(String s) {
        System.out.println("setter 1");
        State.state += "s";
    }

    @Init
    public void init() {
        System.out.println("init");
        State.state += "i";
    }

    @Autowired
    public void set2(Integer i) {
        System.out.println("setter 2");
        State.state += "s";
    }
}
