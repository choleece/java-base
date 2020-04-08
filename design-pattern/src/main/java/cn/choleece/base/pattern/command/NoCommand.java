package cn.choleece.base.pattern.command;

/**
 * @author choleece
 * @Description: 没有任何命令，用于初始化每个按钮，可以省掉对空的判断
 * @Date 2020-04-08 23:11
 **/
public class NoCommand implements Command {

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }
}
