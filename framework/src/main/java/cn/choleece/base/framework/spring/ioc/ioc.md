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

    // 这里为勾子函数，在解析前做什么事情
    preProcessXml(root);
    parseBeanDefinitions(root, this.delegate);
    // 同理，在解析后做什么事情
    postProcessXml(root);

    this.delegate = parent;
}

// 进行解析
protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
    // http://www.springframework.org/schema/beans 判断如果是这个，则往下走逻辑， 搜索阔以发现，这个下边只包含<import/> <beans/> <bean/> <alias/>四个标签
    if (delegate.isDefaultNamespace(root)) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                // 解析default name space 下的标签
                if (delegate.isDefaultNamespace(ele)) {
                    // 实际进行ele解析
                    parseDefaultElement(ele, delegate);
                }
                else {
                    delegate.parseCustomElement(ele);
                }
            }
        }
    }
    else {
        delegate.parseCustomElement(root);
    }
}

// 按照类型进行解析
private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
    // 解析<import/>标签
    if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
        importBeanDefinitionResource(ele);
    }
    // 解析 alias标签
    else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
        processAliasRegistration(ele);
    }
    // 解析bean标签
    else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
        processBeanDefinition(ele, delegate);
    }
    // 解析beans标签
    else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
        // recurse
        doRegisterBeanDefinitions(ele);
    }
}

// 执行bean解析
protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
    // 这一句将ele解析成BeanDefinitionHolder
    BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
    if (bdHolder != null) {
        bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
        try {
            // Register the final decorated instance.
            // 注册bean definition， 将BeanDefinitionHolder注册进来， 下面我们来看看注册是如何进行的
            BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
        }
        catch (BeanDefinitionStoreException ex) {
            getReaderContext().error("Failed to register bean definition with name '" +
                    bdHolder.getBeanName() + "'", ele, ex);
        }
        // Send registration event.
        // 发送一个注册完成的事件
        getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
    }
}

// BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, @Nullable BeanDefinition containingBean) {
    // 获取<bean/>ID属性
    String id = ele.getAttribute(ID_ATTRIBUTE);
    // 获取<bean/>name属性
    String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);

    // 将beanName作为alias
    List<String> aliases = new ArrayList<>();
    if (StringUtils.hasLength(nameAttr)) {
        String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, MULTI_VALUE_ATTRIBUTE_DELIMITERS);
        aliases.addAll(Arrays.asList(nameArr));
    }

    String beanName = id;
    // 如果没有ID，则用别名的第一个作为beanName, 这里的BeanName也是上面的nameAttr
    if (!StringUtils.hasText(beanName) && !aliases.isEmpty()) {
        beanName = aliases.remove(0);
        if (logger.isTraceEnabled()) {
            logger.trace("No XML 'id' specified - using '" + beanName +
                    "' as bean name and " + aliases + " as aliases");
        }
    }

    if (containingBean == null) {
        // 判断相同beanName的是否已经在<beans/>里存在
        checkNameUniqueness(beanName, aliases, ele);
    }

    // 这一步实际解析<bean/>标签，包装成AbstractBeanDefinition
    AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
    if (beanDefinition != null) {
        // 如果beanName为空（也就是没有配置 name & id两个属性），下面就会去取这两个属性
        if (!StringUtils.hasText(beanName)) {
            try {
                if (containingBean != null) {
                    beanName = BeanDefinitionReaderUtils.generateBeanName(
                            beanDefinition, this.readerContext.getRegistry(), true);
                }
                else {
                    beanName = this.readerContext.generateBeanName(beanDefinition);
                    // Register an alias for the plain bean class name, if still possible,
                    // if the generator returned the class name plus a suffix.
                    // This is expected for Spring 1.2/2.0 backwards compatibility.
                    String beanClassName = beanDefinition.getBeanClassName();
                    if (beanClassName != null &&
                            beanName.startsWith(beanClassName) && beanName.length() > beanClassName.length() &&
                            !this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
                        aliases.add(beanClassName);
                    }
                }
                if (logger.isTraceEnabled()) {
                    logger.trace("Neither XML 'id' nor 'name' specified - " +
                            "using generated bean name [" + beanName + "]");
                }
            }
            catch (Exception ex) {
                error(ex.getMessage(), ele);
                return null;
            }
        }
        String[] aliasesArray = StringUtils.toStringArray(aliases);
        
        // 返回BeanDefinitionHolder对象
        return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
    }

    return null;
}

// BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
public static void registerBeanDefinition(
        BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
        throws BeanDefinitionStoreException {

    // Register bean definition under primary name.
    String beanName = definitionHolder.getBeanName();
    // 在这里完成注册
    registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

    // Register aliases for bean name, if any.
    String[] aliases = definitionHolder.getAliases();
    if (aliases != null) {
        for (String alias : aliases) {
            // 维护beanName 和 alias的关系， 用一个map保存 this.aliasMap.put(alias, name);
            registry.registerAlias(beanName, alias);
        }
    }
}

// registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {

    // beanName不能为空
    Assert.hasText(beanName, "Bean name must not be empty");
    // 不能没有BeanDefinition
    Assert.notNull(beanDefinition, "BeanDefinition must not be null");

    if (beanDefinition instanceof AbstractBeanDefinition) {
        try {
            ((AbstractBeanDefinition) beanDefinition).validate();
        }
        catch (BeanDefinitionValidationException ex) {
            throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
                    "Validation of bean definition failed", ex);
        }
    }
    
    // 看看BeanDefinitionMap是否已经注册
    BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
    if (existingDefinition != null) {
        // 如果已经注册，判断最开始的prefreshBeanFactory()方法里设置的是否允许覆盖
        if (!isAllowBeanDefinitionOverriding()) {
            throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
        }
        else if (existingDefinition.getRole() < beanDefinition.getRole()) {
            // e.g. was ROLE_APPLICATION, now overriding with ROLE_SUPPORT or ROLE_INFRASTRUCTURE
            if (logger.isInfoEnabled()) {
                logger.info("Overriding user-defined bean definition for bean '" + beanName +
                        "' with a framework-generated bean definition: replacing [" +
                        existingDefinition + "] with [" + beanDefinition + "]");
            }
        }
        else if (!beanDefinition.equals(existingDefinition)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Overriding bean definition for bean '" + beanName +
                        "' with a different definition: replacing [" + existingDefinition +
                        "] with [" + beanDefinition + "]");
            }
        }
        else {
            if (logger.isTraceEnabled()) {
                logger.trace("Overriding bean definition for bean '" + beanName +
                        "' with an equivalent definition: replacing [" + existingDefinition +
                        "] with [" + beanDefinition + "]");
            }
        }
        // 重新设置
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }
    else {
        // 如果不存在，name这里就要开始进行一些列动作
        if (hasBeanCreationStarted()) {
            // Cannot modify startup-time collection elements anymore (for stable iteration)
            // 这里beanDefinitionMap是concurrentHashMap是线程安全的，在这里放一个synchronized我猜是想保证永远只有一个线程在对其执行操作, 这里不是很确定
            synchronized (this.beanDefinitionMap) {
                this.beanDefinitionMap.put(beanName, beanDefinition);
                List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
                updatedDefinitions.addAll(this.beanDefinitionNames);
                updatedDefinitions.add(beanName);
                this.beanDefinitionNames = updatedDefinitions;
                removeManualSingletonName(beanName);
            }
        }
        else {
            // Still in startup registration phase
            this.beanDefinitionMap.put(beanName, beanDefinition);
            this.beanDefinitionNames.add(beanName);
            removeManualSingletonName(beanName);
        }
        this.frozenBeanDefinitionNames = null;
    }

    if (existingDefinition != null || containsSingleton(beanName)) {
        resetBeanDefinition(beanName);
    }
    else if (isConfigurationFrozen()) {
        clearByTypeCache();
    }
}
```
到这里位置，一个<bean/>已经被解析成BeanDefinition被注册到BeanDefinitionMap里去了，也即ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory(); 这一句完成了。这里只是将xml解析完成，Map里的BeanDefinition还没有初始化，下半场我们将对初始化进行说明

### IOC启动过程，这里再把内容贴一遍
```
public void refresh() throws BeansException, IllegalStateException {
    // 这里加个锁，避免并发启动
    synchronized (this.startupShutdownMonitor) {
        // Prepare this context for refreshing.
        prepareRefresh();

        // Tell the subclass to refresh the internal bean factory.
        // 上文的内容这一句话就分析完了， 接下来往下分析
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
            // 这里是个模版方法，交给子类去实现，具体阔以做一些在bean初始化之前的动作
            onRefresh();

            // Check for listener beans and register them.
            registerListeners();

            // Instantiate all remaining (non-lazy-init) singletons.
            // 这里将对Bean进行初始化，但是不包含一些配置了懒加载的Bean， 是整个的一个重点
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

#### prepareBeanFactory(beanFactory)
```
// 手动设置几个特殊的Bean
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    // Tell the internal bean factory to use the context's class loader etc.
    beanFactory.setBeanClassLoader(getClassLoader());
    beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
    beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

    // Configure the bean factory with context callbacks.
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
    beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
    beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
    beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

    // BeanFactory interface not registered as resolvable type in a plain factory.
    // MessageSource registered (and found for autowiring) as a bean.
    beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
    beanFactory.registerResolvableDependency(ResourceLoader.class, this);
    beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
    beanFactory.registerResolvableDependency(ApplicationContext.class, this);

    // Register early post-processor for detecting inner beans as ApplicationListeners.
    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

    // Detect a LoadTimeWeaver and prepare for weaving, if found.
    if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        // Set a temporary ClassLoader for type matching.
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }

    // Register default environment beans.
    if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
    }
    if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
    }
}
```

#### postProcessBeanFactory(beanFactory)
```
// AbstractRefreshableWebApplicationContext 168行，这里留一个勾子函数，往BeanFacotry里加入一些BeanPostProcessor之类的， 这个只是其中的一个实现
protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
    beanFactory.ignoreDependencyInterface(ServletContextAware.class);
    beanFactory.ignoreDependencyInterface(ServletConfigAware.class);
    WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
    WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext, this.servletConfig);
}
```

#### invokeBeanFactoryPostProcessors(beanFactory)
```
// AbstractApplicationContext 706, 调用BeanFactoryPostProcessor实现类的postProcessBeanFactory(factory)方法
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

    // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
    // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
    if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }
}
```

#### registerBeanPostProcessors(beanFactory)
```
// AbstractApplicationContext 722, 注册实现了BeanPostProcessor接口的实现类，这里注意下BeanFactoryPostProcessor和BeanPostProcessor的区别， 实现了BeanPostProcessor，需要实现两个方法，一个postProcessBeforeInitialization， 
// 一个postProcessAfterInitialization， 这里仅仅是注册， 调用在后文里
protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
}
```

#### initMessageSource()
```
// 初始化当前ApplicationContext的MessageResource， 做一些国际化的处理等
protected void initMessageSource() {
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    if (beanFactory.containsLocalBean(MESSAGE_SOURCE_BEAN_NAME)) {
        this.messageSource = beanFactory.getBean(MESSAGE_SOURCE_BEAN_NAME, MessageSource.class);
        // Make MessageSource aware of parent MessageSource.
        if (this.parent != null && this.messageSource instanceof HierarchicalMessageSource) {
            HierarchicalMessageSource hms = (HierarchicalMessageSource) this.messageSource;
            if (hms.getParentMessageSource() == null) {
                // Only set parent context as parent MessageSource if no parent MessageSource
                // registered already.
                hms.setParentMessageSource(getInternalParentMessageSource());
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Using MessageSource [" + this.messageSource + "]");
        }
    }
    else {
        // Use empty MessageSource to be able to accept getMessage calls.
        DelegatingMessageSource dms = new DelegatingMessageSource();
        dms.setParentMessageSource(getInternalParentMessageSource());
        this.messageSource = dms;
        beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);
        if (logger.isTraceEnabled()) {
            logger.trace("No '" + MESSAGE_SOURCE_BEAN_NAME + "' bean, using [" + this.messageSource + "]");
        }
    }
}
```

#### initApplicationEventMulticaster()
```
// 注册一些EventBean
protected void initApplicationEventMulticaster() {
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
        this.applicationEventMulticaster =
                beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
        if (logger.isTraceEnabled()) {
            logger.trace("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
        }
    }
    else {
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
        if (logger.isTraceEnabled()) {
            logger.trace("No '" + APPLICATION_EVENT_MULTICASTER_BEAN_NAME + "' bean, using " +
                    "[" + this.applicationEventMulticaster.getClass().getSimpleName() + "]");
        }
    }
}
```

#### registerListeners()
```
// 注册一些监听器
protected void registerListeners() {
    // Register statically specified listeners first.
    for (ApplicationListener<?> listener : getApplicationListeners()) {
        getApplicationEventMulticaster().addApplicationListener(listener);
    }

    // Do not initialize FactoryBeans here: We need to leave all regular beans
    // uninitialized to let post-processors apply to them!
    String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
    for (String listenerBeanName : listenerBeanNames) {
        getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
    }

    // Publish early application events now that we finally have a multicaster...
    Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
    this.earlyApplicationEvents = null;
    if (!CollectionUtils.isEmpty(earlyEventsToProcess)) {
        for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
            getApplicationEventMulticaster().multicastEvent(earlyEvent);
        }
    }
}
```

#### finishBeanFactoryInitialization(beanFactory)
```
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
    // Initialize conversion service for this context.
    if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
            beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
        beanFactory.setConversionService(
                beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
    }

    // Register a default embedded value resolver if no bean post-processor
    // (such as a PropertyPlaceholderConfigurer bean) registered any before:
    // at this point, primarily for resolution in annotation attribute values.
    if (!beanFactory.hasEmbeddedValueResolver()) {
        beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
    }

    // Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
    String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
    for (String weaverAwareName : weaverAwareNames) {
        getBean(weaverAwareName);
    }

    // Stop using the temporary ClassLoader for type matching.
    beanFactory.setTempClassLoader(null);

    // Allow for caching all bean definition metadata, not expecting further changes.
    beanFactory.freezeConfiguration();

    // Instantiate all remaining (non-lazy-init) singletons.
    // 在这里开始执行初始化
    beanFactory.preInstantiateSingletons();
}
```

##### beanFactory.preInstantiateSingletons()
```
public void preInstantiateSingletons() throws BeansException {
    if (logger.isTraceEnabled()) {
        logger.trace("Pre-instantiating singletons in " + this);
    }

    // Iterate over a copy to allow for init methods which in turn register new bean definitions.
    // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
    List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);

    // Trigger initialization of all non-lazy singleton beans...
    // 取到系统所有的BeanName, 挨个遍历
    for (String beanName : beanNames) {
        通过BeanName获取BeanDefinition
        RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
        // 如果不是抽象的且是单例且不是懒加载的，就进入到下边的逻辑
        if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
            // 判断是否是工厂Bean，后文会介绍BeanFactory & FactoryBean的区别
            if (isFactoryBean(beanName)) {
                // 工厂bean跟普通bean的区别是bean前边有个&符号
                Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
                if (bean instanceof FactoryBean) {
                    final FactoryBean<?> factory = (FactoryBean<?>) bean;
                    boolean isEagerInit;
                    if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
                        isEagerInit = AccessController.doPrivileged((PrivilegedAction<Boolean>)
                                        ((SmartFactoryBean<?>) factory)::isEagerInit,
                                getAccessControlContext());
                    }
                    else {
                        isEagerInit = (factory instanceof SmartFactoryBean &&
                                ((SmartFactoryBean<?>) factory).isEagerInit());
                    }
                    if (isEagerInit) {
                        getBean(beanName);
                    }
                }
            }
            else {
                // 对于普通的bean，在这里进行初始化
                getBean(beanName);
            }
        }
    }

    // Trigger post-initialization callback for all applicable beans...
    for (String beanName : beanNames) {
        Object singletonInstance = getSingleton(beanName);
        if (singletonInstance instanceof SmartInitializingSingleton) {
            final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    smartSingleton.afterSingletonsInstantiated();
                    return null;
                }, getAccessControlContext());
            }
            else {
                smartSingleton.afterSingletonsInstantiated();
            }
        }
    }
}

public Object getBean(String name) throws BeansException {
    return doGetBean(name, null, null, false);
}

protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
			@Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {

    转换一些beanName
    final String beanName = transformedBeanName(name);
    Object bean;

    // Eagerly check singleton cache for manually registered singletons.
    // 判断bean是否已经实例化
    Object sharedInstance = getSingleton(beanName);
    if (sharedInstance != null && args == null) {
        if (logger.isTraceEnabled()) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                logger.trace("Returning eagerly cached instance of singleton bean '" + beanName +
                        "' that is not fully initialized yet - a consequence of a circular reference");
            }
            else {
                logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
            }
        }
        // 如果是普通bean不是FactoryBean, 那么则直接返回该bean, 如果是FactoryBean, 那么则返回FactoryBean实际产生的Object
        bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
    }

    else {
        // Fail if we're already creating this bean instance:
        // We're assumably within a circular reference.
        if (isPrototypeCurrentlyInCreation(beanName)) {
            // 创建过了此 beanName 的 prototype 类型的 bean，那么抛异常
            throw new BeanCurrentlyInCreationException(beanName);
        }

        // Check if bean definition exists in this factory.
        // 检查以下这个BeanDefinition是否存在，如果不存在，则在父容器内找找，有点类似与类加载器的双亲委派
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            // Not found -> check parent.
            String nameToLookup = originalBeanName(name);
            if (parentBeanFactory instanceof AbstractBeanFactory) {
                return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
                        nameToLookup, requiredType, args, typeCheckOnly);
            }
            else if (args != null) {
                // Delegation to parent with explicit args.
                return (T) parentBeanFactory.getBean(nameToLookup, args);
            }
            else if (requiredType != null) {
                // No args -> delegate to standard getBean method.
                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }
            else {
                return (T) parentBeanFactory.getBean(nameToLookup);
            }
        }

        if (!typeCheckOnly) {
            markBeanAsCreated(beanName);
        }

        // 以上内容还没有创建Bean, 下面的内容，将会对bean进行创建
        try {
            final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
            checkMergedBeanDefinition(mbd, beanName, args);

            // Guarantee initialization of beans that the current bean depends on.
            String[] dependsOn = mbd.getDependsOn();
            if (dependsOn != null) {
                for (String dep : dependsOn) {
                    // 检查下是否有循环的depend on ，跟ref里的循环依赖不一样，这里读者需要细品一下
                    if (isDependent(beanName, dep)) {
                        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                    }
                    // 注册依赖关系
                    registerDependentBean(dep, beanName);
                    try {
                        getBean(dep);
                    }
                    catch (NoSuchBeanDefinitionException ex) {
                        throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                                "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
                    }
                }
            }

            // Create bean instance.
            // 如果是单例，那么这里就要开始创建了
            if (mbd.isSingleton()) {
                sharedInstance = getSingleton(beanName, () -> {
                    try {
                        // 实际执行创建bean的过程
                        return createBean(beanName, mbd, args);
                    }
                    catch (BeansException ex) {
                        // Explicitly remove instance from singleton cache: It might have been put there
                        // eagerly by the creation process, to allow for circular reference resolution.
                        // Also remove any beans that received a temporary reference to the bean.
                        destroySingleton(beanName);
                        throw ex;
                    }
                });
                bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
            }

            else if (mbd.isPrototype()) {
                // It's a prototype -> create a new instance.
                Object prototypeInstance = null;
                try {
                    beforePrototypeCreation(beanName);
                    prototypeInstance = createBean(beanName, mbd, args);
                }
                finally {
                    afterPrototypeCreation(beanName);
                }
                bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
            }

            else {
                String scopeName = mbd.getScope();
                final Scope scope = this.scopes.get(scopeName);
                if (scope == null) {
                    throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                }
                try {
                    Object scopedInstance = scope.get(beanName, () -> {
                        beforePrototypeCreation(beanName);
                        try {
                            return createBean(beanName, mbd, args);
                        }
                        finally {
                            afterPrototypeCreation(beanName);
                        }
                    });
                    bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                }
                catch (IllegalStateException ex) {
                    throw new BeanCreationException(beanName,
                            "Scope '" + scopeName + "' is not active for the current thread; consider " +
                            "defining a scoped proxy for this bean if you intend to refer to it from a singleton",
                            ex);
                }
            }
        }
        catch (BeansException ex) {
            cleanupAfterBeanCreationFailure(beanName);
            throw ex;
        }
    }

    // Check if required type matches the type of the actual bean instance.
    if (requiredType != null && !requiredType.isInstance(bean)) {
        try {
            T convertedBean = getTypeConverter().convertIfNecessary(bean, requiredType);
            if (convertedBean == null) {
                throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
            }
            return convertedBean;
        }
        catch (TypeMismatchException ex) {
            if (logger.isTraceEnabled()) {
                logger.trace("Failed to convert bean '" + name + "' to required type '" +
                        ClassUtils.getQualifiedName(requiredType) + "'", ex);
            }
            throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
        }
    }
    return (T) bean;
}

// return createBean(beanName, mbd, args);
protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
			throws BeanCreationException {

    if (logger.isTraceEnabled()) {
        logger.trace("Creating instance of bean '" + beanName + "'");
    }
    RootBeanDefinition mbdToUse = mbd;

    // Make sure bean class is actually resolved at this point, and
    // clone the bean definition in case of a dynamically resolved Class
    // which cannot be stored in the shared merged bean definition.
    // 确保BeanClass已经被加载
    Class<?> resolvedClass = resolveBeanClass(mbd, beanName);
    if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
        mbdToUse = new RootBeanDefinition(mbd);
        mbdToUse.setBeanClass(resolvedClass);
    }

    // Prepare method overrides.
    try {
        mbdToUse.prepareMethodOverrides();
    }
    catch (BeanDefinitionValidationException ex) {
        throw new BeanDefinitionStoreException(mbdToUse.getResourceDescription(),
                beanName, "Validation of method overrides failed", ex);
    }

    try {
        // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
        // 这一步有机会返回代理类
        Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
        if (bean != null) {
            return bean;
        }
    }
    catch (Throwable ex) {
        throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName,
                "BeanPostProcessor before instantiation of bean failed", ex);
    }

    try {
        // 创建具体的实例
        Object beanInstance = doCreateBean(beanName, mbdToUse, args);
        if (logger.isTraceEnabled()) {
            logger.trace("Finished creating instance of bean '" + beanName + "'");
        }
        return beanInstance;
    }
    catch (BeanCreationException | ImplicitlyAppearedSingletonException ex) {
        // A previously detected exception with proper bean creation context already,
        // or illegal singleton state to be communicated up to DefaultSingletonBeanRegistry.
        throw ex;
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
                mbdToUse.getResourceDescription(), beanName, "Unexpected exception during bean creation", ex);
    }
}

// Object beanInstance = doCreateBean(beanName, mbdToUse, args)
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
			throws BeanCreationException {

    // Instantiate the bean.
    BeanWrapper instanceWrapper = null;
    if (mbd.isSingleton()) {
        instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
    }
    if (instanceWrapper == null) {
        instanceWrapper = createBeanInstance(beanName, mbd, args);
    }
    final Object bean = instanceWrapper.getWrappedInstance();
    Class<?> beanType = instanceWrapper.getWrappedClass();
    if (beanType != NullBean.class) {
        mbd.resolvedTargetType = beanType;
    }

    // Allow post-processors to modify the merged bean definition.
    synchronized (mbd.postProcessingLock) {
        if (!mbd.postProcessed) {
            try {
                applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
            }
            catch (Throwable ex) {
                throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                        "Post-processing of merged bean definition failed", ex);
            }
            mbd.postProcessed = true;
        }
    }

    // Eagerly cache singletons to be able to resolve circular references
    // even when triggered by lifecycle interfaces like BeanFactoryAware.
    // 通过这里解决循环依赖问题
    boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
            isSingletonCurrentlyInCreation(beanName));
    if (earlySingletonExposure) {
        if (logger.isTraceEnabled()) {
            logger.trace("Eagerly caching bean '" + beanName +
                    "' to allow for resolving potential circular references");
        }
        // 这里有三个Map
        /** Cache of singleton objects: bean name to bean instance. */
        // private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
        /** Cache of singleton factories: bean name to ObjectFactory. */
        // private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
        /** Cache of early singleton objects: bean name to bean instance. */
        // private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

        addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
    }

    // Initialize the bean instance.
    Object exposedObject = bean;
    try {
        // 在这里完成属性组装
        populateBean(beanName, mbd, instanceWrapper);
        // 回调init-method方法，BeanPostProcessor的方法
        exposedObject = initializeBean(beanName, exposedObject, mbd);
    }
    catch (Throwable ex) {
        if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
            throw (BeanCreationException) ex;
        }
        else {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
        }
    }

    if (earlySingletonExposure) {
        Object earlySingletonReference = getSingleton(beanName, false);
        if (earlySingletonReference != null) {
            if (exposedObject == bean) {
                exposedObject = earlySingletonReference;
            }
            else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
                String[] dependentBeans = getDependentBeans(beanName);
                Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
                for (String dependentBean : dependentBeans) {
                    if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
                        actualDependentBeans.add(dependentBean);
                    }
                }
                if (!actualDependentBeans.isEmpty()) {
                    throw new BeanCurrentlyInCreationException(beanName,
                            "Bean with name '" + beanName + "' has been injected into other beans [" +
                            StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +
                            "] in its raw version as part of a circular reference, but has eventually been " +
                            "wrapped. This means that said other beans do not use the final version of the " +
                            "bean. This is often the result of over-eager type matching - consider using " +
                            "'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.");
                }
            }
        }
    }

    // Register bean as disposable.
    try {
        registerDisposableBeanIfNecessary(beanName, bean, mbd);
    }
    catch (BeanDefinitionValidationException ex) {
        throw new BeanCreationException(
                mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
    }

    return exposedObject;
}

// exposedObject = initializeBean(beanName, exposedObject, mbd);
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
    if (System.getSecurityManager() != null) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            invokeAwareMethods(beanName, bean);
            return null;
        }, getAccessControlContext());
    }
    else {
        invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        // 实现了BeanPostProcessor接口
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
    }

    try {
        // 定义了initMethod方法, 回调实现了InitializingBean, 调用afterPropertiesSet()
        invokeInitMethods(beanName, wrappedBean, mbd);
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
                (mbd != null ? mbd.getResourceDescription() : null),
                beanName, "Invocation of init method failed", ex);
    }
    if (mbd == null || !mbd.isSynthetic()) {
        
        // 实现了BeanPostProcessor接口
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}

// invokeInitMethods(beanName, wrappedBean, mbd)
protected void invokeInitMethods(String beanName, final Object bean, @Nullable RootBeanDefinition mbd)
			throws Throwable {

    boolean isInitializingBean = (bean instanceof InitializingBean);
    if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
        if (logger.isTraceEnabled()) {
            logger.trace("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
        }
        if (System.getSecurityManager() != null) {
            try {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                    ((InitializingBean) bean).afterPropertiesSet();
                    return null;
                }, getAccessControlContext());
            }
            catch (PrivilegedActionException pae) {
                throw pae.getException();
            }
        }
        else {
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }

    if (mbd != null && bean.getClass() != NullBean.class) {
        String initMethodName = mbd.getInitMethodName();
        if (StringUtils.hasLength(initMethodName) &&
                !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
                !mbd.isExternallyManagedInitMethod(initMethodName)) {
            invokeCustomInitMethod(beanName, bean, mbd);
        }
    }
}

// 属性装配 populateBean(beanName, mbd, instanceWrapper);
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
    if (bw == null) {
        if (mbd.hasPropertyValues()) {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Cannot apply property values to null instance");
        }
        else {
            // Skip property population phase for null instance.
            return;
        }
    }

    // Give any InstantiationAwareBeanPostProcessors the opportunity to modify the
    // state of the bean before properties are set. This can be used, for example,
    // to support styles of field injection.
    if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
                    return;
                }
            }
        }
    }

    PropertyValues pvs = (mbd.hasPropertyValues() ? mbd.getPropertyValues() : null);

    int resolvedAutowireMode = mbd.getResolvedAutowireMode();
    if (resolvedAutowireMode == AUTOWIRE_BY_NAME || resolvedAutowireMode == AUTOWIRE_BY_TYPE) {
        MutablePropertyValues newPvs = new MutablePropertyValues(pvs);
        // Add property values based on autowire by name if applicable.
        if (resolvedAutowireMode == AUTOWIRE_BY_NAME) {
            autowireByName(beanName, mbd, bw, newPvs);
        }
        // Add property values based on autowire by type if applicable.
        if (resolvedAutowireMode == AUTOWIRE_BY_TYPE) {
            autowireByType(beanName, mbd, bw, newPvs);
        }
        pvs = newPvs;
    }

    boolean hasInstAwareBpps = hasInstantiationAwareBeanPostProcessors();
    boolean needsDepCheck = (mbd.getDependencyCheck() != AbstractBeanDefinition.DEPENDENCY_CHECK_NONE);

    PropertyDescriptor[] filteredPds = null;
    if (hasInstAwareBpps) {
        if (pvs == null) {
            pvs = mbd.getPropertyValues();
        }
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                PropertyValues pvsToUse = ibp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
                if (pvsToUse == null) {
                    if (filteredPds == null) {
                        filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
                    }
                    pvsToUse = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
                    if (pvsToUse == null) {
                        return;
                    }
                }
                pvs = pvsToUse;
            }
        }
    }
    if (needsDepCheck) {
        if (filteredPds == null) {
            filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
        }
        checkDependencies(beanName, mbd, filteredPds, pvs);
    }

    if (pvs != null) {
        applyPropertyValues(beanName, mbd, bw, pvs);
    }
}
```