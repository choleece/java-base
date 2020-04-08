package cn.choleece.base.pattern.command;

/**
 * @author choleece
 * @Description: 命令接收者
 * @Date 2020-04-08 23:03
 **/
public class LightReceiver {

    public void on() {
        System.out.println("电灯打开了...");
    }

    public void off() {
        System.out.println("电灯关闭了...");
    }
}
