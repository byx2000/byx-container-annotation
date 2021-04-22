package byx.ioc.factory.test18;

import byx.ioc.annotation.Component;
import byx.ioc.annotation.Value;

@Component
@Value(id = "strVal", value = "hello")
@Value(type = int.class, id = "intVal", value = "123")
@Value(type = double.class, id = "doubleVal", value = "3.14")
@Value(type = char.class, id = "charVal", value = "a")
@Value(type = boolean.class, id = "boolVal", value = "true")
@Value(value = "hi")
@Value(type = double.class, value = "6.28")
@Value(id = "user", type = User.class, value = "User(1001,'byx','123')")
public class A {
}
