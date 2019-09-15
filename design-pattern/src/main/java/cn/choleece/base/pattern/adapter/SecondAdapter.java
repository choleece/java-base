package cn.choleece.base.pattern.adapter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-15 13:08
 **/
public class SecondAdapter implements Target {

    private Adaptee adaptee;

    public SecondAdapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void request() {
        adaptee.specificRequest();
        System.out.println("我是被修饰过");
    }
}
