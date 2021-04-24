# ByxContainerAnnotation

一个基于注解的轻量级IOC容器，支持构造函数注入和字段注入，支持循环依赖处理和检测，具有高可扩展的插件系统。

## Maven引入

```xml
<repositories>
    <repository>
        <id>byx-maven-repo</id>
        <name>byx-maven-repo</name>
        <url>https://gitee.com/byx2000/maven-repo/raw/master/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>byx.ioc</groupId>
        <artifactId>byx-container-annotation</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## 使用示例

通过一个简单的例子来快速了解ByxContainerAnnotation的使用。

A.java：

```java
package byx.test;

import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Autowired;
import byx.ioc.annotation.Component;

@Component
public class A {
    @Autowired
    private B b;

    public void f() {
        b.f();
    }
}

```

B.java：

```java
package byx.test;

import byx.ioc.annotation.Component;

@Component
public class B {
    public void f() {
        System.out.println("hello!");
    }

    @Component
    public String info() {
        return "hi";
    }
}
```

main函数：

```java
public static void main(String[] args) {
    Container container = new AnnotationContainerFactory("byx.test").create();

    A a = container.getObject(A.class);
    a.f();

    String info = container.getObject("info");
    System.out.println(info);
}
```

执行main函数后，控制台输出结果：

```
hello!
hi
```

## 快速入门

如无特殊说明，以下示例中的类均定义在`byx.test`包下。

### AnnotationContainerFactory

该类是`ContainerFactory`接口的实现类，`ContainerFactory`是容器工厂，用于从指定的地方创建IOC容器。

`AnnotationContainerFactory`通过包扫描的方式创建IOC容器，用法如下：

```java
Container container = new AnnotationContainerFactory(/*包名或某个类的Class对象*/).create();
```

在构造`AnnotationContainerFactory`时，需要传入一个包名或者某个类的`Class`对象。调用`create`方法时，该包及其子包下所有标注了`@Component`的类都会被扫描，扫描完成后返回一个`Container`实例。

### Container

该接口是IOC容器的根接口，可以用该接口接收`ContainerFactory`的`create`方法的返回值，内含方法如下：

|方法|描述|
|---|---|
|`void registerObject(String id, ObjectFactory factory)`|向IOC容器注册对象，如果id已存在，则抛出`IdDuplicatedException`|
|`<T> T getObject(String id)`|获取容器中指定id的对象，如果id不存在则抛出`IdNotFoundException`|
|`<T> T getObject(Class<T> type)`|获取容器中指定类型的对象，如果类型不存在则抛出`TypeNotFoundException`，如果存在多于一个指定类型的对象则抛出`MultiTypeMatchException`|
|`Set<String> getObjectIds()`|获取容器中所有对象的id集合|

用法如下：

```java
Container container = new AnnotationContainerFactory(...).create();

// 获取容器中类型为A的对象
A a = container.getObject(A.class);

// 获取容器中id为msg的对象
String msg = container.getObject("msg");
```

### @Component注解

`@Component`注解可以加在类上，用于向IOC容器注册对象。在包扫描的过程中，只有标注了`@Component`注解的类才会被扫描。

例子：

```java
@Component
public class A {}

public class B {}


// 可以获取到A对象
A a = container.getObject(A.class);

// 这条语句执行会抛出TypeNotFoundException
// 因为class B没有标注@Component注解，所以没有被注册到IOC容器中
B b = container.getObject(B.class);
```

`@Component`注解还可以加在方法上，用于向IOC容器注册一个实例方法创建的对象，注册的id为方法名。

例子：

```java
@Component
public class A {
    // 注册了一个id为msg的String
    @Component
    public String msg() {
        return "hello";
    }
}

// msg的值为hello
String msg = container.getObject("msg");
```

注意，如果某个方法被标注了`@Component`，则该方法所属的类也必须标注`@Component`，否则该方法不会被包扫描器扫描。

### @Id注解

`@Id`注解可以加在类上，与`@Component`配合使用，用于指定注册对象时所用的id。

例子：

```java
@Component @Id("a")
public class A {}

// 使用id获取A对象
A a = container.getObject("a");
```

注意，如果类上没有标注`@Id`，则该类注册时的id为该类的全限定类名。

`@Id`注解也可以加在方法上，用于指定实例方法创建的对象的id。

例子：

```java
@Component
public class A {
    // 注册了一个id为msg的String
    @Component @Id("msg")
    public String f() {
        return "hello";
    }
}

// hello
String msg = container.getObject("msg");
```

`@Id`注解还可以加在方法参数和字段上，请看[构造函数注入](#构造函数注入)、[方法参数注入](#方法参数注入)和[@Autowire自动装配](#@Autowire自动装配)。

### 构造函数注入

如果某类只有一个构造函数（无参或有参），则IOC容器在实例化该类的时候会调用该构造函数，并自动从容器中注入构造函数的参数。

例子：

```java
@Component
public class A {
    private final B b;

    // 通过构造函数注入字段b
    public A(B b) {
        this.b = b;
    }
}

@Component
public class B {}

// a被正确地构造，其字段b也被正确地注入
A a = container.getObject(A.class);
```

在构造函数的参数上可以使用`@Id`注解来指定注入的对象id。如果没有标注`@Id`注解，则默认是按照类型注入。

例子：

```java
@Component
public class A {
    private final B b;

    // 通过构造函数注入id为b1的对象
    public A(@Id("b1") B b) {
        this.b = b;
    }
}

public class B {}

@Component @Id("b1")
public class B1 extends B {}

@Component @Id("b2")
public class B2 extends B {}

// 此时a中的b注入的是B1的实例
A a = container.getObject(A.class);
```

对于有多个构造函数的类，需要使用`@Autowire`注解标记实例化所用的构造函数。

例子：

```java
@Component
public class A {
    private Integer i;
    private String s;

    public A(Integer i) {
        this.i = i;
    }

    // 使用这个构造函数来创建A对象
    @Autowire
    public A(String s) {
        this.s = s;
    }
}

@Component
public class B {
    @Component
    public Integer f() {
        return 123;
    }

    @Component
    public String g() {
        return "hello";
    }
}

// 使用带String参数的构造函数实例化的a
A a = container.getObject(A.class);
```

注意，不允许同时在多个构造函数上标注`@Autowire`注解。

### @Autowired自动装配

`@Autowired`注解标注在对象中的字段上，用于直接注入对象的字段。

例子：

```java
@Component
public class A {
    @Autowired
    private B b;
}

@Component
public class B {}

// a中的字段b被成功注入
A a = container.getObject(A.class);
```

默认情况下，`@Autowired`按照类型注入。`@Autowired`也可以配合`@Id`一起使用，实现按照id注入。

例子：

```java
@Component
public class A {
    // 注入id为b1的对象
    @Autowired @Id("b1")
    private B b;
}

public class B {}

@Component @Id("b1")
public class B1 extends B {}

@Component @Id("b2")
public class B2 extends B {}

// a中的字段b注入的是B1的对象
A a = container.getObject(A.class);
```

`@Autowired`还可标注在构造函数上，请看[构造函数注入](#构造函数注入)。

### 方法参数注入

如果标注了`@Component`的实例方法带有参数列表，则这些参数也会从容器自动注入，注入规则与构造函数的参数注入相同。

例子：

```java
@Component
public class A {
    // 该方法的所有参数都从容器中获得
    @Component
    public String s(@Id("s1") String s1, @Id("s2") String s2) {
        return s1 + " " + s2;
    }
}

@Component
public class B {
    @Component
    public String s1() {
        return "hello";
    }

    @Component
    public String s2() {
        return "hi";
    }
}

// s的值为：hello hi
String s = container.getObject("s");
```

### @Init注解

`@Init`注解用于指定对象的初始化方法，该方法在对象属性填充后、创建代理对象前创建。

例子：

```java
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

// 获取a对象
A a = container.getObject(A.class);
```

输出如下：

```
constructor
setter 1
setter 2
init
```

## 循环依赖

ByxContainerAnnotation支持各种循环依赖的处理和检测，以下是一些例子。

一个对象的循环依赖：

```java
@Component
public class A {
    @Autowired
    private A a;
}

public static void main(String[] args) {
    Container container = new AnnotationContainerFactory("byx.test").create();

    // a被成功创建并初始化
    A a = container.getObject(A.class);
}
```

两个对象的循环依赖：

```java
@Component
public class A {
    @Autowired
    private B b;
}

@Component
public class B {
    @Autowired
    private A a;
}

public static void main(String[] args) {
    Container container = new AnnotationContainerFactory("byx.test").create();

    // a和b都被成功创建并初始化
    A a = container.getObject(A.class);
    B b = container.getObject(B.class);
}
```

构造函数注入与字段注入混合的循环依赖：

```java
@Component
public class A {
    private final B b;

    public A(B b) {
        this.b = b;
    }
}

@Component
public class B {
    @Autowired
    private A a;
}

public static void main(String[] args) {
    Container container = new AnnotationContainerFactory("byx.test").create();

    // a和b都被成功创建并初始化
    A a = container.getObject(A.class);
    B b = container.getObject(B.class);
}
```

三个对象的循环依赖：

```java
@Component
public class A {
    @Autowired
    private B b;
}

@Component
public class B {
    @Autowired
    private C c;
}

@Component
public class C {
    @Autowired
    private A a;
}

public static void main(String[] args) {
    Container container = new AnnotationContainerFactory("byx.test").create();

    // a、b、c都被成功创建并初始化
    A a = container.getObject(A.class);
    B b = container.getObject(B.class);
    C c = container.getObject(C.class);
}
```

无法解决的循环依赖：

```java
@Component
public class A {
    private final B b;

    public A(B b) {
        this.b = b;
    }
}

@Component
public class B {
    private final A a;

    public B(A a) {
        this.a = a;
    }
}

public static void main(String[] args) {
    Container container = new AnnotationContainerFactory("byx.test").create();

    // 抛出CircularDependencyException异常
    A a = container.getObject(A.class);
    B b = container.getObject(B.class);
}
```

## 扩展

ByxContainer提供了一个灵活的插件系统，你可以通过引入一些名称为`byx-container-extension-*`的依赖来扩展ByxContainer的功能。当然，你也可以编写自己的扩展。

### 当前已有的扩展

|扩展|说明|
|---|---|
|[byx-container-extension-aop](https://github.com/byx2000/byx-container-extension-aop)|提供面向切面编程（AOP）的支持，包括前置增强（`@Before`）、后置增强（`@After`）、环绕增强（`@Around`）、异常增强（`@AfterThrowing`）四种增强类型|
|[byx-container-extension-transaction](https://github.com/byx2000/byx-container-extension-transaction)|提供声明式事务的支持，包括对`JdbcUtils`和`@Transactional`注解的支持|

### 自己编写扩展

`AnnotationContainerFactory`对外提供两个扩展点：

* `ContainerCallback`接口

    该接口定义如下：

    ```java
    public interface ContainerCallback {
        void afterContainerInit(Container container);

        default int getOrder() {
            return 1;
        }
    }
    ```

    `ContainerCallback`类似于Spring的`BeanFactoryPostProcessor`。`afterContainerInit`方法会在包扫描结束后回调，用户可通过创建该接口的实现类来动态地向容器中注册额外的组件。

    当存在多个`ContainerCallback`时，它们调用的先后顺序取决于`getOrder`返回的顺序值，数字小的先执行。

* `ObjectCallback`接口

    该接口定义如下：

    ```java
    public interface ObjectCallback{
        default void afterObjectInit(ObjectCallbackContext ctx) {

        }

        default Object afterObjectWrap(ObjectCallbackContext ctx) {
            return ctx.getObject();
        }

        default int getOrder() {
            return 1;
        }
    }
    ```

    `ObjectCallback`类似于Spring的`BeanPostProcessor`。`afterObjectInit`方法会在对象初始化后（即属性填充后）回调，`afterObjectWrap`方法会在代理对象创建后回调。

    当存在多个`ObjectCallback`时，它们调用的先后顺序取决于`getOrder`返回的顺序值，数字小的先执行。

编写ByxContainer扩展的步骤：

1. 定义一个或多个`ContainerCallback`和`ObjectCallback`的实现类，这些实现类需要有可访问的默认构造函数

2. 在`resources`目录下创建一个名为`byx-container-extension.properties`的文件，该文件声明了需要导出的组件，包含的键值如下：

    |键值|含义|
    |---|---|
    |`containerCallback`|所有`ContainerCallback`的全限定类名，用`,`分隔|
    |`objectCallback`|所有`ObjectCallback`的全限定类名，用`,`分隔|

3. 将该项目打包成Jar包或Maven依赖，在主项目（即引入了byx-container-annotation的项目）中引入，即可启用自定义的回调组件
