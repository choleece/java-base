package cn.choleece.base.jdk;

/**
 * @author choleece
 * @Description: 对象分配
 * @Date 2020-11-10 21:19
 **/
public class ObjectAllocate {

    public static void main(String[] args) {
        AllocateDemo demo = getDemoInstance();
        System.out.println("在main内分配: " + demo);
        foo();
//        System.gc();
    }

    private static AllocateDemo getDemoInstance() {
        AllocateDemo demo = new AllocateDemo();
        System.out.println("在static方法内分配: " + demo);
        return demo;
    }

    public static void foo() {
        // 如果不是执行System.gc()，说明finalize没有被调用，说明不是随着方法出栈而销毁，说明不是此对象实例不是在栈上分配（此时"逃逸分析"没起作用，事实上逃逸分析阔以参考：https://zhuanlan.zhihu.com/p/36247966）
        AllocateDemo demo = new AllocateDemo();
        System.out.println("在foo内分配: " + demo);
    }

    static class AllocateDemo {

        @Override
        protected void finalize() throws Throwable {
            System.out.println("我在这里被回收了...");
            super.finalize();
        }
    }
}
