package cn.choleece.base.pattern.bridge;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-22 23:57
 **/
public class Client {

    public static void main(String[] args) {
        Phone phone = new FoldPhone(new XiaoMi());
        phone.open();
        phone.call();
        phone.close();

        System.out.println("--------------");

        phone = new FoldPhone(new Vivo());
        phone.open();
        phone.call();
        phone.close();

        System.out.println("--------------");

        phone = new FoldPhone(new XiaoMi());
        phone.open();
        phone.call();
        phone.close();

        System.out.println("--------------");

        phone = new UpRightPhone(new Vivo());
        phone.open();
        phone.call();
        phone.close();
    }

}
