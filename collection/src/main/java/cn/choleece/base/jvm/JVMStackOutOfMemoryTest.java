package cn.choleece.base.jvm;

/**
 * @description: 如果虚拟机在扩展栈时，如果无法申请更多的内存，那么就会报OutOfMemoryError错误
 * @author: sf
 * @time: 2019-10-21 16:53
 *
 * 调优经验：如果物理内存为2GB，JVM允许设置配置最大堆内存（Xmx）、最大方法区容量（MaxPermSize），用2GB - Xmx - MaxPermSize - 程序计数器内存（此区域是JVM里唯一一个没有规定OutOfMemoryError的区域，占用很小，可以忽略）
 * 得到虚拟机栈内存 + 本地方法栈内存 + 虚拟机进程本身消耗掉的内存，如果虚拟机本身运行内存不计，那么就剩下虚拟机栈 + 本地方法栈。
 * 一般虚拟机栈的栈深度达到1000～2000完全没有问题，如果出现了线程创建过多，导致OutOfMemory，可以适当考虑下"减小"堆内存。
 */
public class JVMStackOutOfMemoryTest {

    public void doSomeThing() {
        while (true) {}
    }

    public void stackLeakByThread() {
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doSomeThing();
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        JVMStackOutOfMemoryTest outOfMemoryTest = new JVMStackOutOfMemoryTest();
        outOfMemoryTest.stackLeakByThread();
    }
}
