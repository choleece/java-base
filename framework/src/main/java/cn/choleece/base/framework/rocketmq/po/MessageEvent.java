package cn.choleece.base.framework.rocketmq.po;

import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @description: 监听对象
 * @author: choleece
 * @time: 2019-12-28 22:28
 */
@Data
public class MessageEvent extends ApplicationEvent {

    private static final long serialVersionUID = -4468405250074063206L;
    private DefaultMQPushConsumer consumer;
    private List<MessageExt> msgs;

    public MessageEvent(List<MessageExt> msgs, DefaultMQPushConsumer consumer) throws Exception {
        super(msgs);
        this.consumer = consumer;
        this.setMsgs(msgs);
    }
}
