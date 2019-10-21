package cn.choleece.base.jvm;

/**
 * @description: Java 虚拟机栈 抛StackOverFlow异常测试
 * @author: sf
 * @time: 2019-10-21 16:46
 *
 * 调整参数: -Xss
 *
 * 如果线程请求的栈深度超过JVM锁允许的最大深度，则会抛出StackOverFlowError
 *
 * 参考: https://blog.csdn.net/lldouble/article/details/79217819
 */
public class JVMStackOverFlowTest {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;

        // 利用递归测试虚拟机允许的最大深度
        stackLeak();
    }

    public static void main(String[] args) {
        JVMStackOverFlowTest overFlowTest = new JVMStackOverFlowTest();

        try {
            overFlowTest.stackLeak();
        } catch (Throwable e) {
            // 打印19386 每次运行结果不一致，但总会有一个值
            System.out.println("stack length: " + overFlowTest.stackLength);
            e.printStackTrace();
        }
    }
}
