package cn.choleece.base.pattern.bridge;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-22 23:55
 **/
public class FoldPhone extends Phone {

    public FoldPhone(Brand brand) {
        super(brand);
    }

    @Override
    protected void open() {
        super.open();
        System.out.println("折叠手机");
    }

    @Override
    protected void close() {
        super.close();
        System.out.println("折叠手机");
    }

    @Override
    protected void call() {
        super.call();
        System.out.println("折叠手机");
    }
}
