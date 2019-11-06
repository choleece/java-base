package cn.choleece.base.jvm;

/**
 * JVN 参考 https://crossoverjie.top/JCSprout/#/jvm/MemoryAllocation
 * @author choleece
 * @Description: Java 双亲委派类加载机制
 * @Date 2019-10-13 16:08
 * 参考：https://www.jianshu.com/p/202f6abb229c
 * https://blog.csdn.net/m0_38075425/article/details/81627349
 * 双亲委派：
 * 双亲委派模式的工作原理的是;如果一个类加载器收到了类加载请求，它并不会自己先去加载，而是把这个请求委托给父类的加载器去执行，如果父类加载器还存在其父类加载器，
 * 则进一步向上委托，依次递归，请求最终将到达顶层的启动类加载器，如果父类加载器可以完成类加载任务，就成功返回，倘若父类加载器无法完成此加载任务，子加载器才会尝试自己去加载，这就是双亲委派模式
 *
 * 双亲委派优点：
 * 1. 高效，避免重复加载类
 * Java类随着它的类加载器一起具备了一种带有优先级的层次关系，通过这种层级关可以避免类的重复加载，当父亲已经加载了该类时，就没有必要子ClassLoader再加载一次
 * 2. 安全，保证Java核心类库不会被篡改
 * java核心api中定义类型不会被随意替换，假设通过网络传递一个名为java.lang.Integer的类，通过双亲委托模式传递到启动类加载器，而启动类加载器在核心Java API发现这个名字的类，
 * 发现该类已被加载，并不会重新加载网络传递的过来的java.lang.Integer，而直接返回已加载过的Integer.class，这样便可以防止核心API库被随意篡改
 *
 * 类加载器(JVM 预定义的类加载器有三类)
 * 1. 根类加载器(Bootstrap ClassLoader) 它用来加载 Java 的核心类，是用原生代码来实现的，并不继承自 java.lang.ClassLoader（负责加载$JAVA_HOME中jre/lib/rt.jar里所有的class，由C++实现，不是ClassLoader子类）
 * 2. 扩展类加载器(Extensions ClassLoader) 它负责加载JRE的扩展目录，lib/ext或者由java.ext.dirs系统属性指定的目录中的JAR包的类。由Java语言实现，父类加载器为null
 * 3. 应用程序类加载器(Application ClassLoader) 被称为系统（也称为应用）类加载器，它负责在JVM启动时加载来自Java命令的-classpath选项、java.class.path系统属性，或者CLASSPATH换将变量所指定的JAR包和类路径
 **/
public class ClassLoaderTest {

    static {
        System.out.println("是否初始化啊。。。。。");
    }

    /**
     * 此方法可以查看各类加载器
     *
     * 下面链接是一个自定义类加载器的例子
     * https://www.jianshu.com/p/f2ae00d64fa6
     * @param args
     */
    public static void main(String[] args) {
        ClassLoader classLoader1 = ClassLoaderTest.class.getClassLoader();
        ClassLoader classLoader2 = classLoader1.getParent();
        ClassLoader classLoader3 = classLoader2.getParent();

        System.out.println(classLoader1);
        System.out.println(classLoader2);
        System.out.println(classLoader3);

        try {
            /**
             * clazz 底层调用的是Class.forName(className,true,classloader) 第二个参数为是否初始化，默认为true
             * clazz.forName结果显示会进行初始化操作，就是会执行静态代码块等操作
             *
             * 这也是为什么JDBC的数据库链接会使用Class.forName("package name")
             */
            Class clazz = Class.forName("cn.choleece.base.jvm.ClassLoaderTest");
            System.out.println(clazz);

            /**
             * 结果证明Classloader.loadClass()不会进行初始化工作，即不会执行静态代码块
             */
            Class clazz1 = Thread.currentThread().getContextClassLoader().loadClass("cn.choleece.base.jvm.ClassForNameTest");
            System.out.println(clazz1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
