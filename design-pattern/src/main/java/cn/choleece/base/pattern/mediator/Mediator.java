package cn.choleece.base.pattern.mediator;

/**
 * @author choleece
 * @Description: 中介者
 * @Date 2020-04-25 16:04
 **/
public abstract class Mediator {

    /**
     * 注册同事集合
     * @param colleague
     * @param colleagueName
     */
    public abstract void register(String colleagueName, Colleague colleague);

    /**
     * 接收colleague的消息
     * @param stateChange
     * @param colleagueName
     */
    public abstract void getMessage(int stateChange, String colleagueName);
}
