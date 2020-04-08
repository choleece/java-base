package cn.choleece.base.pattern.command;

/**
 * @author choleece
 * @Description: 具体的命令
 * @Date 2020-04-08 23:05
 **/
public class LightOnCommand implements Command {

    private LightReceiver receiver;

    public LightOnCommand(LightReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.on();
    }

    @Override
    public void undo() {
        receiver.off();
    }
}
