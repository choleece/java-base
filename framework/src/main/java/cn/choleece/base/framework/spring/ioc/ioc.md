## IOC 源码分析
IOC(Inversion of Controller)本质是一个容器， 这个容器在Spring里也即BeanFactory(它是一个基础的容器，只是提供一些基础的操作方法，它还有很多的派生类)，
管理者系统里的bean；Bean可以理解成就是一个一个的实例，此实例可以是一个普通的对象，也可以是不同对象的复合组成的对象，后续会将Bean的在Spring中的定义，也即BeanDefinition, 这些数据也属于IOC的MetaData，元数据，我个人理解为描述数据的数据。

### Bean配置的方式
- XML 通过XML文件进行配置，系统提供ClassPathXmlApplicationContext, FileSystemXmlApplicationContext进行配置（大胆点想，其实是任何可以按照格式描述的文件，不限涞源，也可以来自与网络，因为从这里解析的，只是我们需要获取到Bean的MedaData）
```
package cn.choleece.base.framework.spring.ioc;

# 公共的参考类
public class HelloWorldBean {
    public void sayHello() {
        System.out.println("hello world...");
    }
}
```

```
<bean id="hello" class="cn.choleece.base.framework.spring.ioc.HelloWorldBean"/>

ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
HelloWorldBean contextBean = (HelloWorldBean) context.getBean("hello");
contextBean.sayHello();
```

- Component 方式配置, 也叫基于注解的方式
```
package cn.choleece.base.framework.spring.ioc;

@Component
public class HelloWorldComponent {
    public void sayHello() {
        System.out.println("hello world...");
    }
}

# 此处是用注解配置，可以想象一下，这里的AnnotationConfigApplicationContext 和 上文的ClassPathXmlApplicationContext的子接口， 如果想扩展，这里可以有其他的子接口进行实现
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(HelloWorldComponent.class);

HelloWorldComponent helloWorldComponent = (HelloWorldComponent) applicationContext.getBean("hello");
helloWorldComponent.sayHello();
```

- 基于Java 配置的方式
```
@Configuration
public class JavaConfig {
    @Bean(value = "helloWorldBean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }
}

# 这里跟上文是一样的，可以想象，里边做的事情，应该就是统一的从类中获取数据，组装成MetaData，不管是通过xml, component，还是configure & bean的形式，最终的结果，是为了获取MetaData
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaConfig.class);

HelloWorldBean javaBean = (HelloWorldBean) applicationContext.getBean("helloWorldBean");
javaBean.sayHello();
```
