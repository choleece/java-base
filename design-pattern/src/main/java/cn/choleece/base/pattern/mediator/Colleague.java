package cn.choleece.base.pattern.mediator;

import lombok.Data;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-25 16:02
 **/
@Data
public abstract class Colleague {

    private String name;

    private Mediator mediator;

    public Colleague(String name, Mediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public void sendMessage(int state) {
        mediator.getMessage(state, name);
    }
}
