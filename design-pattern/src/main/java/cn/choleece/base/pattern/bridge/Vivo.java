package cn.choleece.base.pattern.bridge;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-22 23:52
 **/
public class Vivo implements Brand {

    @Override
    public void open() {
        System.out.println("vivo手机开机");
    }

    @Override
    public void close() {
        System.out.println("vivo手机开机");
    }

    @Override
    public void call() {
        System.out.println("vivo手机开机");
    }
}
