package cn.choleece.base.pattern.adapter;

/**
 * @Description: 适配器（Adapter）类：它是一个转换器，通过继承或引用适配者的对象，把适配者接口转换成目标接口，让客户按目标接口的格式访问适配者。
 * @Author choleece
 * @Date 2019-09-15 13:04
 **/
public class Adapter extends Adaptee implements Target {

    @Override
    public void specificRequest() {
        super.specificRequest();
    }

    @Override
    public void request() {
        specificRequest();

        System.out.println("我被修饰过");
    }
}
