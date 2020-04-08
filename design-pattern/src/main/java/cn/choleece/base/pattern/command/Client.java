package cn.choleece.base.pattern.command;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-08 23:22
 **/
public class Client {

    public static void main(String[] args) {
        LightReceiver receiver = new LightReceiver();

        LightOnCommand lightOnCommand = new LightOnCommand(receiver);
        LightOffCommand lightOffCommand = new LightOffCommand(receiver);

        RemoteController controller = new RemoteController();

        // 第一灯具命令接收
        controller.setCommand(0, lightOnCommand, lightOffCommand);
        controller.onButtonClick(0);
        System.out.println("执行第一次撤销...");
        controller.undoButtonClick();

        controller.offButtonClick(0);
        System.out.println("执行第二次次撤销...");
        controller.undoButtonClick();
    }
}
