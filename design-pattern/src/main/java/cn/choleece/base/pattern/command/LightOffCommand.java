package cn.choleece.base.pattern.command;

/**
 * @author choleece
 * @Description: 具体的命令
 * @Date 2020-04-08 23:05
 **/
public class LightOffCommand implements Command {

    private LightReceiver receiver;

    public LightOffCommand(LightReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.off();
    }

    @Override
    public void undo() {
        receiver.on();
    }
}
