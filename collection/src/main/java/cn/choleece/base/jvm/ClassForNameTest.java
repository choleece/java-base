package cn.choleece.base.jvm;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-06 23:21
 **/
public class ClassForNameTest {
    static {
        System.out.println("看看classloader.loadClass（）是否会执行");
    }
}
