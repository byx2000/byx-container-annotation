package byx.ioc;

import byx.ioc.core.Container;
import byx.ioc.core.ObjectDefinition;
import byx.ioc.core.SimpleContainer;
import byx.ioc.exception.IdDuplicatedException;
import byx.ioc.exception.IdNotFoundException;
import byx.ioc.exception.MultiTypeMatchException;
import byx.ioc.exception.TypeNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 基本用法
 */
public class SimpleContainerTest1 {
    /**
     * 根据id获取对象、根据类型获取对象、异常
     */
    @Test
    public void test1() {
        ObjectDefinition f1 = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return String.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return "hello";
            }
        };

        ObjectDefinition f2 = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return Integer.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return 123;
            }
        };

        ObjectDefinition f3 = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return Double.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return 3.14;
            }
        };

        ObjectDefinition f4 = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return Double.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return 6.28;
            }
        };

        Container container = new SimpleContainer();
        container.registerObject("f1", f1);
        container.registerObject("f2", f2);
        assertThrows(IdDuplicatedException.class, () -> container.registerObject("f1", f1));
        container.registerObject("f3", f3);
        container.registerObject("f4", f4);

        String s = container.getObject(String.class);
        assertEquals("hello", s);

        s = container.getObject("f1", String.class);
        assertEquals("hello", s);

        Integer i = container.getObject(Integer.class);
        assertEquals(123, i);

        i = container.getObject("f2", Integer.class);
        assertEquals(123, i);

        String s2 = container.getObject("f1");
        assertSame(s, s2);

        Integer i2 = container.getObject("f2");
        assertSame(i, i2);

        assertThrows(IdNotFoundException.class, () -> container.getObject("f5"));
        assertThrows(TypeNotFoundException.class, () -> container.getObject(Boolean.class));
        assertThrows(MultiTypeMatchException.class, () -> container.getObject(Double.class));
        assertThrows(IdNotFoundException.class, () -> container.getObject("aaa", String.class));
        assertThrows(TypeNotFoundException.class, () -> container.getObject("f1", Integer.class));
    }

    /**
     * 通过父类类型获取子类对象
     */
    @Test
    public void test2() {
        Container container = new SimpleContainer();

        ObjectDefinition f = new ObjectDefinition() {
            @Override
            public Class<?> getType() {
                return String.class;
            }

            @Override
            public Object getInstance(Object[] params) {
                return "hello";
            }
        };

        container.registerObject("f", f);

        CharSequence s = container.getObject(CharSequence.class);
        assertEquals("hello", s);
    }
}
