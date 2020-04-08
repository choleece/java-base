package cn.choleece.base.pattern.command;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-08 23:13
 **/
public class RemoteController {

    /**
     * 按钮命令数组
     */
    Command[] onCommands;
    Command[] offCommands;

    /**
     * 撤销命令
     */
    Command undoCommand;

    public RemoteController() {
        onCommands = new Command[5];
        offCommands = new Command[5];

        for (int i = 0; i < 5; i++) {
            onCommands[i] = new NoCommand();
            offCommands[i] = new NoCommand();
        }
    }

    public void setCommand(int idx, Command onCommand, Command offCommand) {
        onCommands[idx] = onCommand;
        offCommands[idx] = offCommand;
    }

    public void onButtonClick(int idx) {
        onCommands[idx].execute();

        // 记住，用于撤销
        undoCommand = onCommands[idx];
    }

    public void offButtonClick(int idx) {
        offCommands[idx].execute();

        // 记住，用于撤销
        undoCommand = offCommands[idx];
    }

    public void undoButtonClick() {
        undoCommand.undo();


    }
}
