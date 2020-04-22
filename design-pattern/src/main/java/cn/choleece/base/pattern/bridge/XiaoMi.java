package cn.choleece.base.pattern.bridge;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-22 23:51
 **/
public class XiaoMi implements Brand {

    @Override
    public void open() {
        System.out.println("小米手机开机");
    }

    @Override
    public void close() {
        System.out.println("小米手机关机");
    }

    @Override
    public void call() {
        System.out.println("小米手机大电话");
    }
}
