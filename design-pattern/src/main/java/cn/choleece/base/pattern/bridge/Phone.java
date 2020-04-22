package cn.choleece.base.pattern.bridge;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-22 23:53
 **/
public abstract class Phone {

    private Brand brand;

    public Phone(Brand brand) {
        this.brand = brand;
    }

    protected void open() {
        brand.open();
    }

    protected void close() {
        brand.close();
    }

    protected void call() {
        brand.call();
    }
}
