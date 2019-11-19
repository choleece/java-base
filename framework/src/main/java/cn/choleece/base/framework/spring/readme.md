# Spring 执行流程
## Spring IOC配置方式大致可以有三种
1. 基于XML的配置
```
<bean id="innerHello" class="cn.choleece.base.framework.spring.ioc.HelloWorldBean.Hello"/>
``` 
2. 基于Java的方式
```
@Configuration
public class Config {
    
    @Bean
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }
}
```
3. 基于注解的形式
```
@Component("hello")
public class HelloWorldComponent {

    public void sayHello() {
        System.out.println("hello annotation");
    }

}
```

## bean 的定义
1. class 指定创建类的bean
2. name 指定bean的唯一表示， 在xml配置里，可由id/name指定
3. lazy-initialization 是否懒加载 如果设置的是，那么就是在使用的时候再初始化
4. scope bean的作用域 有5个，singleton(单例 默认的情况)、prototype(多例，每次从容器里请求bean时，都是new 一个)、request(每次http请求都会创建新的bean)、session(同一个http session是同一个bean)、global-session

## bean的生命周期
1.bean的定义 ---> 2.bean的初始化 ---> 3.bean的使用 ---> 4.bean的销毁

### 初始化回调
org.springframework.beans.factory.InitializingBean 接口指定一个单一的方法,或者使用@PostContruct
```
void afterPropertiesSet() throws Exception;
```
一个bean实现InitializingBean接口，然后实现afterPropertiesSet()方法, 如
```
@Configuration
public class JavaConfig implements InitializingBean {

    @Bean
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化完成后...");
    }
}

@Component("hello")
public class HelloWorldComponent {

    public void sayHello() {
        System.out.println("hello annotation");
    }

    @PostConstruct
    public void init() {
        System.out.println("init...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("destroy...");
    }

}

// 或者xml里的配置指定init 方法

<bean id="hello" class="cn.choleece.base.framework.spring.ioc.HelloWorldBean" init-method="init">
    <property name="name" value="choleece"/>
</bean>

public class HelloWorldBean {

    static private String name;

    public void sayHello() {
        System.out.println("hello, " + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Hello {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void sayHello() {
            System.out.println("inner static class, hello " + name);
        }
    }

    public void init() {
        System.out.println("初始化完成后...");
    }
}
```

### 销毁回调
org.springframework.beans.factory.DisposableBean 接口指定一个单一的方法，或者使用@PreDestroy
```
void destroy() throws Exception;
```
一个bean实现DisposableBean接口，然后重写destroy方法，如
```
@Configuration
public class JavaConfig implements InitializingBean, DisposableBean {

    @Bean
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化完成后...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("bean销毁后...");
    }
}

@Component("hello")
public class HelloWorldComponent {

    public void sayHello() {
        System.out.println("hello annotation");
    }

    @PostConstruct
    public void init() {
        System.out.println("init...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("destroy...");
    }

}

// 基于xml的配置方法，指定destroy-method
<bean id="hello" class="cn.choleece.base.framework.spring.ioc.HelloWorldBean" init-method="init" destroy-method="destroy">
    <property name="name" value="choleece"/>
</bean>
```

### Bean 后置处理器
实现BeanPostProcessor接口，然后重写其中的两个方法，如下:
```
/**
 * 在初始化之前做某些事情
 * @param bean
 * @param beanName
 * @return
 * @throws BeansException
 */
@Override
public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("postProcessBeforeInitialization...");
    return null;
}

/**
 * 在初始化之后做某些事情
 * @param bean
 * @param beanName
 * @return
 * @throws BeansException
 */
@Override
public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("postProcessAfterInitialization....");
    return null;
}
```

## bean 初始化过程
1. 实例化对象 ---> 2.设置对象属性 ---> 3.检查Ware相关接口，并设置相关依赖 ---> 4. BeanPostProcessor前置处理(postProcessBeforeInitialization 此方法) ---> 
5. 检查是否实现了InitializingBean,以决定是否调用afterPropertiesSet方法 ---> 6. 检查是否有自定义的initMethod方法 ---> 7. BeanPostProcessor后置处理(postProcessAfterInitialization 此方法) ---> 
8. 注册必要的Destruction相关的回调接口 ---> 9. 使用中 ---> 10. 是否实现里DisposableBean，以决定是否需要执行destroy方法 ---> 11. 检查是否有自定义的destroyMethod方法

## 分析bean 加载过程
1. AbstractApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");从xml里加载bean
```
// configLocations为xml配置文件的classpath文件，组成一个string 数组
public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, @Nullable ApplicationContext parent) throws BeansException {
    super(parent);
    // 从xml里读取配置文件，读取配置文件完了后，执行refresh方法
    this.setConfigLocations(configLocations);
    if (refresh) {
        this.refresh();
    }
}

// 设置文件配置, 将class path 地址文件放入到configLocations数组里
public void setConfigLocations(@Nullable String... locations) {
    if (locations != null) {
        Assert.noNullElements(locations, "Config locations must not be null");
        this.configLocations = new String[locations.length];

        for(int i = 0; i < locations.length; ++i) {
            this.configLocations[i] = this.resolvePath(locations[i]).trim();
        }
    } else {
        this.configLocations = null;
    }

}
```

2. 初始化-刷新容器
```
public void refresh() throws BeansException, IllegalStateException {
    synchronized(this.startupShutdownMonitor) {
        
        // 准备刷新 初始化earlyApplicationListeners 空间，具体是个LinkedHashSet
        this.prepareRefresh();
        // 初始化bean factory
        ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
        // 准备bean factory
        this.prepareBeanFactory(beanFactory);

        try {
            // bean factory 前置处理
            this.postProcessBeanFactory(beanFactory);
            
            this.invokeBeanFactoryPostProcessors(beanFactory);
            this.registerBeanPostProcessors(beanFactory);
            this.initMessageSource();
            this.initApplicationEventMulticaster();
            this.onRefresh();
            this.registerListeners();
            this.finishBeanFactoryInitialization(beanFactory);
            this.finishRefresh();
        } catch (BeansException var9) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + var9);
            }

            this.destroyBeans();
            this.cancelRefresh(var9);
            throw var9;
        } finally {
            this.resetCommonCaches();
        }

    }
}
```
