# IOC 源码分析
IOC(Inversion of Controller)本质是一个容器， 这个容器在Spring里也即BeanFactory(它是一个基础的容器，只是提供一些基础的操作方法，它还有很多的派生类)，
管理者系统里的bean；Bean可以理解成就是一个一个的实例，此实例可以是一个普通的对象，也可以是不同对象的复合组成的对象，后续会将Bean的在Spring中的定义，也即BeanDefinition, 这些数据也属于IOC的MetaData，元数据，我个人理解为描述数据的数据。
## Bean配置的方式
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

### Bean的定义
前边我们看到，配置Bean的方式大致有三种，但是这三种其实对应的都是一个东西,Metadata，元数据。配置的目的是为了更好的去描述元数据，那么我们就需要去定义
这个元数据，看看这个元数据到底长什么样子。在Spring中，Bean的定义由BeanDefinition来进行包装，下面我们就一探究竟，看看这个BeanDefinition包含哪些数据，是否跟XML配置里的属性一致。

```
package org.springframework.beans.factory.config

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {
	// Modifiable attributes

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 */
	@Nullable
	String getParentName();

	// 设置Bean的一个全限定类名 com.xxx.xxx.HelloWorldBean
	void setBeanClassName(@Nullable String beanClassName);
	@Nullable
	String getBeanClassName();

    // 设置Bean的类型/或者作用范围，比如单例，多例，或者同一个session是同一个实例等等
	void setScope(@Nullable String scope);
	String getScope();

    // 设置是否懒加载 如果设置的是false, 那么当容器已经启动之后，这个就会实例化
	void setLazyInit(boolean lazyInit);
	boolean isLazyInit();

    // 设置依赖的bean，当容器中存在依赖的bean的时候，此Bean才生效， 下文将讲解一些关键的注解
	void setDependsOn(@Nullable String... dependsOn);

    // 获取依赖的bean数组
	@Nullable
	String[] getDependsOn();

    // 设置bean是否是自动装配
	void setAutowireCandidate(boolean autowireCandidate);
	boolean isAutowireCandidate();

    // 当一个bean注册了多个之后，这个确定是否是首选滴
	void setPrimary(boolean primary);
	boolean isPrimary();

    // 配置/获取设置了FactoryBean的名字, 这里要区分下FactoryBean 和 BeanFactory， 这里后文也会进行讲解
	void setFactoryBeanName(@Nullable String factoryBeanName);
	@Nullable
	String getFactoryBeanName();
	void setFactoryMethodName(@Nullable String factoryMethodName);

    // 设置工厂方法，与FactoryBean成对出现
	@Nullable
	String getFactoryMethodName();

    // 获取构造函数配置的参数
	ConstructorArgumentValues getConstructorArgumentValues();
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

    // 获取属性集合
	MutablePropertyValues getPropertyValues();
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}

    // 设置/获取init method 的名字，这里是个勾子函数
	void setInitMethodName(@Nullable String initMethodName);
	@Nullable
	String getInitMethodName();

    // 设置/获取Bean销毁前执行的方法名称， 这里也是个勾子函数
	void setDestroyMethodName(@Nullable String destroyMethodName);
	@Nullable
	String getDestroyMethodName();

	/**
	 * Set the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @since 5.1
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	void setRole(int role);

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	int getRole();

	/**
	 * Set a human-readable description of this bean definition.
	 * @since 5.1
	 */
	void setDescription(@Nullable String description);

	/**
	 * Return a human-readable description of this bean definition.
	 */
	@Nullable
	String getDescription();


	// Read-only attributes

	/**
	 * Return a resolvable type for this bean definition,
	 * based on the bean class or other specific metadata.
	 * <p>This is typically fully resolved on a runtime-merged bean definition
	 * but not necessarily on a configuration-time definition instance.
	 * @return the resolvable type (potentially {@link ResolvableType#NONE})
	 * @since 5.2
	 * @see ConfigurableBeanFactory#getMergedBeanDefinition
	 */
	ResolvableType getResolvableType();

    // 是否是单例
	boolean isSingleton();
    
    // 是否为原型， 也即多例
	boolean isPrototype();

	/**
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 */
     // 是否是抽象的
	boolean isAbstract();

	/**
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 */
	@Nullable
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * <p>Allows for retrieving the decorated bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 */
    // 如果这个Bean是个代理对象，这个方法用于获取它原始的Bean
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();
}
```

以上内容为一个Bean所对应的原始数据，可以看到BeanDefinition是个接口，实际组装的是它的实现类，BeanDefinition只是定义了一些基本方法，但是通过方法名，
我们是可以了解到一个Bean的MetaData是包含哪些属性的。下面是一个BeanDefinition的例子，例子里有一些IOC启动时候的影子，大家先大致了解哈。
```
public class BeanDefinitionTest {

    public static void main(String[] args) {
        // 新建一个工厂
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 新建一个 bean definition
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(HelloWorldBean.class)
                .setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE)
                .getBeanDefinition();

        // 注册到工厂
        factory.registerBeanDefinition("helloWorld", beanDefinition);

        // 自己定义一个 bean post processor. 作用是在 bean 初始化之后, 判断这个 bean 如果实现了 ApplicationContextAware 接口, 就把 context 注册进去..(先不要管 context 哪来的...例子嘛)
        factory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ApplicationContextAware) {
                    GenericApplicationContext context = new GenericApplicationContext(factory);
                    ((ApplicationContextAware) bean).setApplicationContext(context);
                }
                return bean;
            }
        });

        // 再注册一个 bean post processor: AutowiredAnnotationBeanPostProcessor. 作用是搜索这个 bean 中的 @Autowired 注解, 生成注入依赖的信息.
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(factory);
        factory.addBeanPostProcessor(autowiredAnnotationBeanPostProcessor);

        HelloWorldBean helloWorldBean = factory.getBean("helloWorld", HelloWorldBean.class);
        helloWorldBean.sayHello();
    }
}
```

### 使用@Configuration Java方式配置的时候常用的一些注解
- @Configuration 用于替代spring-application-context.xml里配置<beans/>这样的文件，一般在配置类上增加此注解，如：
```
@Configuration
public class JavaConfig {

    @Bean(value = "helloWorldBean", initMethod = "init", destroyMethod = "destroy")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }

    public void init() {
        System.out.println("init");
    }
}
```
- @Conditional 用来Spring Bean或者@Configuration配置，只有当某条件生效的时候，Bean或者配置才生效
- @ConditionalOnBean 组合@Conditional，只有当IOC容器中存在指定的Bean的时候，Bean或者配置才生效
- @ConditionalOnMissingBean 组合@Conditional, 作用与@ConditionalOnBean相反
- @ConditionalOnClass 组合@Conditional， 只有当IOC容器中存在指定的Class的时候，Bean或者配置才生效
- @ConditionalOnMissingClass 组合@Conditional， 作用与@ConditionalOnClass相反
- @ConditionalOnWebApplication 组合@Conditional, 只有当应用为WEB应用的时候才生效，应用类型大致可以分为这么几类
```
enum Type {

    /**
     * Any web application will match.
     */
    ANY,

    /**
     * Only servlet-based web application will match.
     */
    SERVLET,

    /**
     * Only reactive-based web application will match.
     */
    REACTIVE

}
```
- @ConditionalOnNotWebApplication 组合@Conditional， 作用与@ConditionalOnWebApplication相反
- @ConditionalOnProperty 组合@Conditional， 只有当属性值指定的时候，Bean或配置才生效， 例如
```
@Configuration
@EnableConfigurationProperties({ZkCuratorProperty.class})
@ConditionalOnProperty(prefix = "choleece.zookeeper.curator", name = "server")
public class ZkConfiguration {
    @Autowired
    private ZkCuratorProperty property;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                property.getServer(),
                property.getSessionTimeoutMs(),
                property.getConnectionTimeoutMs(),
                new RetryNTimes(property.getMaxRetries(), property.getSleepMsBetweenRetries()));
    }
}
```
- @ConfigurationProperties 用来加载额外的配置，直接将配置文件映射成对象，可以应用在类或者Bean上，如
```
@Data
@ConfigurationProperties(prefix = "choleece.zookeeper.curator")
public class ZkCuratorProperty {
    /**
     * zk 地址ip:port
     */
    private String server;

    /**
     * session超时时间
     */
    private Integer sessionTimeoutMs;

    /**
     * 连接超时时间
     */
    private Integer connectionTimeoutMs;

    /**
     * 最大重试次数
     */
    private Integer maxRetries;

    /**
     * 重试间隔时间
     */
    private Integer sleepMsBetweenRetries;
}
```
- @EnableConfigurationProperties 用来配合@ConfigurationProperties， 只有当注解了EnableConfigurationProperties才会生效。例如，使用以下两种方式，都阔以在IOC容器中注入Bean
    
    方式一：
    ```
    // 利用Component注解，可以直接将ZkCuratorProperty配置到IOC容器内，要使用的话可以直接用@Autowired的方式进行注入
    @Data
    @Component
    @ConfigurationProperties(prefix = "choleece.zookeeper.curator")
    public class ZkCuratorProperty {
        /**
         * zk 地址ip:port
         */
        private String server;
    
        /**
         * session超时时间
         */
        private Integer sessionTimeoutMs;
    
        /**
         * 连接超时时间
         */
        private Integer connectionTimeoutMs;
    
        /**
         * 最大重试次数
         */
        private Integer maxRetries;
    
        /**
         * 重试间隔时间
         */
        private Integer sleepMsBetweenRetries;
    }
    ```
  
    方式二：
    ```
    @Data
    @ConfigurationProperties(prefix = "choleece.zookeeper.curator")
    public class ZkCuratorProperty {
        /**
         * zk 地址ip:port
         */
        private String server;
    
        /**
         * session超时时间
         */
        private Integer sessionTimeoutMs;
    
        /**
         * 连接超时时间
         */
        private Integer connectionTimeoutMs;
    
        /**
         * 最大重试次数
         */
        private Integer maxRetries;
    
        /**
         * 重试间隔时间
         */
        private Integer sleepMsBetweenRetries;
    }

    // 采用Configuration + EnableConfigurationProperties的方式也阔以起到同样的作用
    @Configuration
    @EnableConfigurationProperties(ZkCuratorProperty.class)
    public class ConfigurationClass {}
    ```
 
 - @Import 一共可以提供三种方式进入配置Bean， @Import只能作用在类上
 
    方式一：直接在类上注入
    ```
    public class BaseClassOne {
    }
   
    public class BaseClassTwo {
    } 
   
    @Import({BaseClassOne.class, BaseClassTwo.class})
    public class ImportTypeOne {
    }
   
    public static void main(String[] args) {
        // 这里的参数代表要做操作的类
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ImportTypeOne.class);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames){
            System.out.println(name);
        }
    }
   
    // 输出：发现除了importTypeOne本身外，另外两个类也注入进来了
    importTypeOne
    cn.choleece.base.framework.spring.annotation.BaseClassOne
    cn.choleece.base.framework.spring.annotation.BaseClassTwo
    ```
   
   方式二：通过实现ImportSelector的形式， 这一共比较重要，这里可以动态的去注入一些bean
   ```
   public class ImportTypeTwo implements ImportSelector {
   
       @Override
       public String[] selectImports(AnnotationMetadata importingClassMetadata) {
           // 这里可以返回空数组，但是不能返回NULL，否则会NPE
   //        return new String[0];
   
           // 这里返回类的全限定名的数组，所以这里可以是动态的， 这个动态特性很重要，可以当成可配置的参数来进行使用，达到动态配置Bean到IOC容器内
           // 包装不同，增加判断条件，返回不同的数组
           return new String[] {"cn.choleece.base.framework.spring.annotation.BaseClassOne"};
       }
   }
   
   @Import(ImportTypeTwo.class)
   public class BeanImport {
   }
   
   public static void main(String[] args) {
       // 这里的参数代表要做操作的类
       AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanImport.class);

       String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
       for (String name : beanDefinitionNames){
           System.out.println(name);
       }
   }
  
   // 输出：发现，当通过import导入之后，IOC容器内会注入一个在selectImports方法返回的数组内的Bean
   beanImport
   cn.choleece.base.framework.spring.annotation.BaseClassOne
   ```
   
   方式三：通过实现ImportBeanDefinitionRegistrar接口，来手动进行注入
   
   ```
   public class ImportTypeTwo implements ImportSelector {
      
      @Override
      public String[] selectImports(AnnotationMetadata importingClassMetadata) {
          // 这里可以返回空数组，但是不能返回NULL，否则会NPE
          // return new String[0];
  
          // 这里返回类的全限定名的数组，所以这里可以是动态的， 这个动态特性很重要，可以当成可配置的参数来进行使用，达到动态配置Bean到IOC容器内
          return new String[] {"cn.choleece.base.framework.spring.annotation.BaseClassOne"};
      }
    }
  
   @Import(ImportTypeTwo.class)
      public class BeanImport {
   }
  
   public static void main(String[] args) {
      // 这里的参数代表要做操作的类
      AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanImport.class);

      String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
      for (String name : beanDefinitionNames){
          System.out.println(name);
      }
   }
 
   // 输出：发现，当通过import导入之后，IOC容器内会注入一个自己手动注入的Bean
   beanImport   
   customBean
   ```
   
   以上为@Import的三种用法，基于以上用法，可以有很多的扩展，比如我们常见的@Enable***之类的注解，就是建立在@Import之上， 下面我看一个例子
   ```
   // 定义一个注解，注解的作用就是执行@Import，我们来看看Import里的类是干什么
   @Target(ElementType.TYPE)
   @Retention(RetentionPolicy.RUNTIME)
   @Import(SchedulingConfiguration.class)
   @Documented
   public @interface EnableScheduling {
   }
   
   // SchedulingConfiguration 是个Configuration类, 里边可以注入@Bean，从而向IOC容器类注册Bean
   @Configuration
   @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
   public class SchedulingConfiguration {
   
   	@Bean(name = TaskManagementConfigUtils.SCHEDULED_ANNOTATION_PROCESSOR_BEAN_NAME)
   	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
   	public ScheduledAnnotationBeanPostProcessor scheduledAnnotationProcessor() {
   		return new ScheduledAnnotationBeanPostProcessor();
   	}
   }
   
   @Target(ElementType.TYPE)
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   @Import(AspectJAutoProxyRegistrar.class)
   public @interface EnableAspectJAutoProxy {
   	boolean proxyTargetClass() default false;
   	boolean exposeProxy() default false;
   }
   
   @Override
   public void registerBeanDefinitions(
   		AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
   
   	AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
   
   	AnnotationAttributes enableAspectJAutoProxy =
   			AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
   	if (enableAspectJAutoProxy != null) {
   		if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
   			AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
   		}
   		if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
   			AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
   		}
   	}
   }
   ```
- @EnableAutoConfiguration注解的原理, 会发现这个注解是引用了@Import注解，注入的是AutoConfigurationImportSelector
    ```
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @AutoConfigurationPackage
    @Import(AutoConfigurationImportSelector.class)
    public @interface EnableAutoConfiguration {
    
        String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
    
        /**
         * Exclude specific auto-configuration classes such that they will never be applied.
         * @return the classes to exclude
         */
        Class<?>[] exclude() default {};
    
        /**
         * Exclude specific auto-configuration class names such that they will never be
         * applied.
         * @return the class names to exclude
         * @since 1.3.0
         */
        String[] excludeName() default {};
    }
    ```
  看到这个AutoConfigurationImportSelector， 内心是不是觉得有些许熟悉， 猜测它是@Import的第二种类型，实现ImportSelector接口，我们来看看源码，这里发生了啥
    ```
    // 发现此类是实现了DeferredImportSelector接口，这个接口继承了ImportSelector， 猜测正确
    public class AutoConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware,
            ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered {
    
        @Override
        public String[] selectImports(AnnotationMetadata annotationMetadata) {
            if (!isEnabled(annotationMetadata)) {
                return NO_IMPORTS;
            }
            AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
                    .loadMetadata(this.beanClassLoader);
            AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(autoConfigurationMetadata,
                    annotationMetadata);
            // 这里返回全限定名的类数组
            return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
        }
    }
    
    protected AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata,
                AnnotationMetadata annotationMetadata) {
        if (!isEnabled(annotationMetadata)) {
            return EMPTY_ENTRY;
        }
        AnnotationAttributes attributes = getAttributes(annotationMetadata);
        
        // 这个方法看着有点像返回结果
        List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
        configurations = removeDuplicates(configurations);
        Set<String> exclusions = getExclusions(annotationMetadata, attributes);
        checkExcludedClasses(configurations, exclusions);
        configurations.removeAll(exclusions);
        configurations = filter(configurations, autoConfigurationMetadata);
        fireAutoConfigurationImportEvents(configurations, exclusions);
        return new AutoConfigurationEntry(configurations, exclusions);
    }
    
    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
        List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
                getBeanClassLoader());
        Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
                + "are using a custom packaging, make sure that file is correct.");
        return configurations;
    }
    
    public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
        String factoryTypeName = factoryType.getName();
        return (List)loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
    }
    
    private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
        MultiValueMap<String, String> result = (MultiValueMap)cache.get(classLoader);
        if (result != null) {
            return result;
        } else {
            try {
                // 会发现这里其实是去读取spring.factories下配置的类名，挨个进行解析， 这里有点类似SPI，用ServiceLoader去加载一样，但是不是同一个东西，ServiceLoader是直接加载类，这里只是单纯的返回全限定类名数组
                Enumeration<URL> urls = classLoader != null ? classLoader.getResources("META-INF/spring.factories") : ClassLoader.getSystemResources("META-INF/spring.factories");
                LinkedMultiValueMap result = new LinkedMultiValueMap();
    
                while(urls.hasMoreElements()) {
                    URL url = (URL)urls.nextElement();
                    UrlResource resource = new UrlResource(url);
                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                    Iterator var6 = properties.entrySet().iterator();
    
                    while(var6.hasNext()) {
                        Entry<?, ?> entry = (Entry)var6.next();
                        String factoryTypeName = ((String)entry.getKey()).trim();
                        String[] var9 = StringUtils.commaDelimitedListToStringArray((String)entry.getValue());
                        int var10 = var9.length;
    
                        for(int var11 = 0; var11 < var10; ++var11) {
                            String factoryImplementationName = var9[var11];
                            result.add(factoryTypeName, factoryImplementationName.trim());
                        }
                    }
                }
    
                cache.put(classLoader, result);
                return result;
            } catch (IOException var13) {
                throw new IllegalArgumentException("Unable to load factories from location [META-INF/spring.factories]", var13);
            }
        }
    }
    ```
看到这几个方法，我们就能大致明白EnableAutoConfigure具体是做的一个什么事情。

好了，常用注解也罗列了一些，下边我们来继续分析IOC的注册原理吧。

## IOC启动过程(参考：https://javadoop.com/post/spring-ioc)
```
// 全部的启动流程代码都在这里， 下边我们来一个一个方法的分析，内容较长，慢慢一起分析，一起成长，中间可能有些错误的，欢迎讨论
public void refresh() throws BeansException, IllegalStateException {
    // 这里加个锁，避免并发启动
    synchronized (this.startupShutdownMonitor) {
        // Prepare this context for refreshing.
        prepareRefresh();

        // Tell the subclass to refresh the internal bean factory.
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // Prepare the bean factory for use in this context.
        prepareBeanFactory(beanFactory);

        try {
            // Allows post-processing of the bean factory in context subclasses.
            postProcessBeanFactory(beanFactory);

            // Invoke factory processors registered as beans in the context.
            invokeBeanFactoryPostProcessors(beanFactory);

            // Register bean processors that intercept bean creation.
            registerBeanPostProcessors(beanFactory);

            // Initialize message source for this context.
            initMessageSource();

            // Initialize event multicaster for this context.
            initApplicationEventMulticaster();

            // Initialize other special beans in specific context subclasses.
            onRefresh();

            // Check for listener beans and register them.
            registerListeners();

            // Instantiate all remaining (non-lazy-init) singletons.
            finishBeanFactoryInitialization(beanFactory);

            // Last step: publish corresponding event.
            finishRefresh();
        }

        catch (BeansException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Exception encountered during context initialization - " +
                        "cancelling refresh attempt: " + ex);
            }

            // Destroy already created singletons to avoid dangling resources.
            destroyBeans();

            // Reset 'active' flag.
            cancelRefresh(ex);

            // Propagate exception to caller.
            throw ex;
        }

        finally {
            // Reset common introspection caches in Spring's core, since we
            // might not ever need metadata for singleton beans anymore...
            resetCommonCaches();
        }
    }
}
```

### prepareRefresh
```
protected void prepareRefresh() {
    // Switch to active. 记录一些启动前的准备，启动时间，启动状态等
    this.startupDate = System.currentTimeMillis();
    this.closed.set(false);
    this.active.set(true);

    // 日志相关
    if (logger.isDebugEnabled()) {
        if (logger.isTraceEnabled()) {
            logger.trace("Refreshing " + this);
        }
        else {
            logger.debug("Refreshing " + getDisplayName());
        }
    }

    // Initialize any placeholder property sources in the context environment.
    initPropertySources();

    // Validate that all properties marked as required are resolvable:
    // see ConfigurablePropertyResolver#setRequiredProperties
    getEnvironment().validateRequiredProperties();

    // Store pre-refresh ApplicationListeners...
    if (this.earlyApplicationListeners == null) {
        this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
    }
    else {
        // Reset local application listeners to pre-refresh state.
        this.applicationListeners.clear();
        this.applicationListeners.addAll(this.earlyApplicationListeners);
    }

    // Allow for the collection of early ApplicationEvents,
    // to be published once the multicaster is available...
    this.earlyApplicationEvents = new LinkedHashSet<>();
}
```

### obtainFreshBeanFactory 获取BeanFactory， 这一步非常关键，注意作用是将定义的Bean翻译成BeanDefinition设置到容器中，并返回ConfigurableListableBeanFactory该BeanFactory， 下面来开始分析这个方法
```
// Tell the subclass to refresh the internal bean factory.
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

/**
* Tell the subclass to refresh the internal bean factory.
* @return the fresh BeanFactory instance
* @see #refreshBeanFactory()
* @see #getBeanFactory()
*/
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    // 重置BeanFactory， 在这里会做一些初始化的动作，并且会对Bean进行解析
    refreshBeanFactory();
    return getBeanFactory();
}

# AbstractRefreshableApplicationContext 121行
protected final void refreshBeanFactory() throws BeansException {
    // 判断this.beanFactory != null，如果创建了beanFactory之后，就将对应的bean进行销毁，然后执行closeBeanFactory
    if (hasBeanFactory()) {
        // 保存bean的各种Map进行clear， 然后调用bean定义的destory方法(如果定义了的话)
        destroyBeans();
        // 将this.beanFactory = null即可
        closeBeanFactory();
    }
    try {
        // new DefaultListableBeanFactory(getInternalParentBeanFactory()); 返回DefaultListableBeanFactory, 读者可以参考哈上边标题里的连接，这个BeanFactory的地位还是挺重要的
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        // BeanFactory 序列化
        beanFactory.setSerializationId(getId());
        // 设置是否可以覆盖和是否可以循环依赖
        customizeBeanFactory(beanFactory);
        // 加载BeanDefinition到BeanFactory中
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    } catch (IOException ex) {
        throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
    }
}
```

#### customizeBeanFactory 的主要作用为设置是否可以覆盖和是否可以循环依赖
```
// AbstractRefreshableApplicationContext 214行
protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
    // 是否允许BeanDefinition可以覆盖
    if (this.allowBeanDefinitionOverriding != null) {
        beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
    }
    // 是否允许循环引用, 循环引用这里后边也是需要介绍的内容
    if (this.allowCircularReferences != null) {
        beanFactory.setAllowCircularReferences(this.allowCircularReferences);
    }
}
``` 

#### loadBeanDefinitions 加载各个来源的BeanDefinition到BeanFactory中
```
// AbstractXmlApplication 81行
protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
    // Create a new XmlBeanDefinitionReader for the given BeanFactory.
    // 创建一个全新到XmlBeanDefinitionReader， 发现XmlBeanDefinitionReader的构造函数的参数其实是一个BeanDefinitionRegistry, 这个类里提供了很多注册BeanDefinition的方法， 后续如果有通过手动注册的时候，就需要用到此类或者其子类
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

    // Configure the bean definition reader with this context's
    // resource loading environment.
    beanDefinitionReader.setEnvironment(this.getEnvironment());
    beanDefinitionReader.setResourceLoader(this);
    beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

    // Allow a subclass to provide custom initialization of the reader,
    // then proceed with actually loading the bean definitions.
    // 初始化
    initBeanDefinitionReader(beanDefinitionReader);
    // 实际加载BeanDefinition 到 BeanFactory中，这个beanDefinitionReader其实是包装了一层BeanFactory, 从其构造函数可以看出，其持有BeanFactory对象
    loadBeanDefinitions(beanDefinitionReader);
}
```

##### loadBeanDefinitions(BeanDefinitionReader) 执行加载XML来源的BeanDefinition
```
// 这里有两个分支，可以走到reader.laodBeanDefinitions里， 往下分析会发现，location执行到最后，底层也是回归到调用resource的方法里
protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
    Resource[] configResources = getConfigResources();
    if (configResources != null) {
        reader.loadBeanDefinitions(configResources);
    }
    String[] configLocations = getConfigLocations();
    if (configLocations != null) {
        reader.loadBeanDefinitions(configLocations);
    }
}

// 返回加载的BeanDefinition的个数
public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
    Assert.notNull(resources, "Resource array must not be null");
    int count = 0;
    for (Resource resource : resources) {
        count += loadBeanDefinitions(resource);
    }
    return count;
}

public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
    Assert.notNull(encodedResource, "EncodedResource must not be null");
    if (logger.isTraceEnabled()) {
        logger.trace("Loading XML bean definitions from " + encodedResource);
    }

    Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();

    if (!currentResources.add(encodedResource)) {
        throw new BeanDefinitionStoreException(
                "Detected cyclic loading of " + encodedResource + " - check your import definitions!");
    }

    try (InputStream inputStream = encodedResource.getResource().getInputStream()) {
        InputSource inputSource = new InputSource(inputStream);
        if (encodedResource.getEncoding() != null) {
            inputSource.setEncoding(encodedResource.getEncoding());
        }
        // 实际进行加载的位置
        return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
    }
    catch (IOException ex) {
        throw new BeanDefinitionStoreException(
                "IOException parsing XML document from " + encodedResource.getResource(), ex);
    }
    finally {
        currentResources.remove(encodedResource);
        if (currentResources.isEmpty()) {
            this.resourcesCurrentlyBeingLoaded.remove();
        }
    }
}

protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
			throws BeanDefinitionStoreException {

    try {
        // 将XML解析成Document
        Document doc = doLoadDocument(inputSource, resource);
        
        // 执行注册
        int count = registerBeanDefinitions(doc, resource);
        if (logger.isDebugEnabled()) {
            logger.debug("Loaded " + count + " bean definitions from " + resource);
        }
        return count;
    }
    catch (BeanDefinitionStoreException ex) {
        throw ex;
    }
    catch (SAXParseException ex) {
        throw new XmlBeanDefinitionStoreException(resource.getDescription(),
                "Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
    }
    catch (SAXException ex) {
        throw new XmlBeanDefinitionStoreException(resource.getDescription(),
                "XML document from " + resource + " is invalid", ex);
    }
    catch (ParserConfigurationException ex) {
        throw new BeanDefinitionStoreException(resource.getDescription(),
                "Parser configuration exception parsing XML from " + resource, ex);
    }
    catch (IOException ex) {
        throw new BeanDefinitionStoreException(resource.getDescription(),
                "IOException parsing XML document from " + resource, ex);
    }
    catch (Throwable ex) {
        throw new BeanDefinitionStoreException(resource.getDescription(),
                "Unexpected exception parsing XML document from " + resource, ex);
    }
}

public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
    BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
    int countBefore = getRegistry().getBeanDefinitionCount();
    // 实际执行注册
    documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
    return getRegistry().getBeanDefinitionCount() - countBefore;
}

public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
    this.readerContext = readerContext;
    // 执行注册
    doRegisterBeanDefinitions(doc.getDocumentElement());
}

protected void doRegisterBeanDefinitions(Element root) {
    BeanDefinitionParserDelegate parent = this.delegate;
    this.delegate = createDelegate(getReaderContext(), root, parent);

    if (this.delegate.isDefaultNamespace(root)) {
        String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
        if (StringUtils.hasText(profileSpec)) {
            String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
                    profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
            // We cannot use Profiles.of(...) since profile expressions are not supported
            // in XML config. See SPR-12458 for details.
            if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec +
                            "] not matching: " + getReaderContext().getResource());
                }
                return;
            }
        }
    }

    preProcessXml(root);
    parseBeanDefinitions(root, this.delegate);
    postProcessXml(root);

    this.delegate = parent;
}
```