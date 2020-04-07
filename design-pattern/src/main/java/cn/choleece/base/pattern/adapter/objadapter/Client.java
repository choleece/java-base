package cn.choleece.base.pattern.adapter.objadapter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-07 22:34
 **/
public class Client {

    public static void main(String[] args) {

        System.out.println("对象适配器，不再采用继承的方式");
        Phone phone = new Phone();
        phone.charging(new VoltageAdapter(new Voltage220V()));
    }
}
