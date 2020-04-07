package cn.choleece.base.pattern.adapter.classadapter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-07 22:34
 **/
public class Client {

    public static void main(String[] args) {
        System.out.println("类适配器");
        Phone phone = new Phone();
        phone.charging(new VoltageAdapter());
    }
}
