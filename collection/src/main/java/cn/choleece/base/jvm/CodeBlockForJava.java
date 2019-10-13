package cn.choleece.base.jvm;

/**
 * @author choleece
 * @Description: 类初始化的顺序
 * Java类加载的过程：
 * 参考：https://blog.csdn.net/m0_38075425/article/details/81627349
 * 加载(Loading) -> 链接(Linking)[包含三部分 验证(Verification) -> 准备(Preparation) -> 解析(Resolution)] -> 初始化(Initialization) -> 使用(Using) -> 卸载(Unloading)
 * 参考:《深入理解Java虚拟机》周志明 著 第三部分 虚拟机执行子系统 P210
 * @Date 2019-10-13 16:10
 **/
public class CodeBlockForJava extends BaseCodeBlock {

    {
        System.out.println("这里是子类的普通方法快");
    }

    public CodeBlockForJava() {
        System.out.println("这里是子类的构造方法");
    }

    @Override
    public void msg() {
        System.out.println("这里是子类普通方法");
    }

    public static void staticMsg() {
        System.out.println("这里是子类静态方法");
    }

    static {
        System.out.println("这里子类是静态代码块");
    }

    /**
     * 子类属性
     */
    OtherOne one = new OtherOne();

    /**
     * 子类静态属性
     */
    public static OtherThree three= new OtherThree();

    /**
     * 运行结果：
     * 这里是父类的静态代码块
     * 这里子类是静态代码块
     * 初始化父类属性值
     * 这里是父类的普通方法块
     * 这里是父类的构造方法
     * 这里是子类的普通方法快
     * 初始化子类属性值
     * 这里是子类的构造方法
     * 这里是子类普通方法
     *
     * 结论：如果涉及到父类，子类，父类的"同级别"总是优先于子类执行，后边的优先级都是建立在此基础上的
     * 优先执行静态方法块（带有static的方法块）或者静态属性（这两类同级，看代码谁先谁后）-> 类的属性值初始化或者非静态方法块（这两类同级，看代码谁先谁后） -> 类的构造函数 -> 普通方法
     * @param args
     */
    public static void main(String[] args) {
        BaseCodeBlock block = new CodeBlockForJava();
        block.msg();
    }
}

class BaseCodeBlock {

    public BaseCodeBlock() {
        System.out.println("这里是父类的构造方法");
    }

    public void msg() {
        System.out.println("这里是父类的普通方法");
    }

    public static void staticMsg() {
        System.out.println("这里是父类的静态方法");
    }

    /**
     * 父类静态属性
     */
    public static OtherFour four = new OtherFour();

    static {
        System.out.println("这里是父类的静态代码块");
    }

    /**
     * 父类属性
     */
    OtherTwo two = new OtherTwo();

    {
        System.out.println("这里是父类的普通方法块");
    }
}

class OtherOne {
    public OtherOne() {
        System.out.println("初始化子类属性值");
    }
}

class OtherTwo {
    public OtherTwo() {
        System.out.println("初始化父类属性值");
    }
}

class OtherThree {
    public OtherThree() {
        System.out.println("初始化子类静态属性");
    }
}

class OtherFour {
    public OtherFour() {
        System.out.println("初始化父类静态属性");
    }
}