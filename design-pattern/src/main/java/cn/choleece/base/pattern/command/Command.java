package cn.choleece.base.pattern.command;

/**
 * @author choleece
 * @Description: 命令角色，需要执行的所有命令都在这里，这里阔以是接口，也可以是抽象类
 * @Date 2020-04-08 23:02
 **/
public interface Command {

    /**
     * 执行某个命令
     */
    void execute();

    /**
     * 撤销操作
     */
    void undo();

}
