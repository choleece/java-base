package cn.choleece.base.pattern.bridge;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-22 23:56
 **/
public class UpRightPhone extends Phone {

    public UpRightPhone(Brand brand) {
        super(brand);
    }

    @Override
    protected void open() {
        super.open();
        System.out.println("直板手机");
    }

    @Override
    protected void close() {
        super.close();
        System.out.println("直板手机");
    }

    @Override
    protected void call() {
        super.call();
        System.out.println("直板手机");
    }
}
