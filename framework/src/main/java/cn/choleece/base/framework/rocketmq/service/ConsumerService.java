package cn.choleece.base.framework.rocketmq.service;

import cn.choleece.base.framework.rocketmq.po.MessageEvent;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-12-28 22:49
 */
@Component
public class ConsumerService {

    @EventListener(condition = "#event.msgs[0].topic=='user_topic' && #event.msgs[0].tags=='white'")
    public void rocketmqMsgListener(MessageEvent event) {
        try {
            List<MessageExt> msgs = event.getMsgs();
            for (MessageExt msg : msgs) {
                System.out.println(String.format("消费消息: %s", new String(msg.getBody())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
