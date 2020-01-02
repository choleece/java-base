package cn.choleece.base.framework.rocketmq.controller;

import cn.choleece.base.framework.rocketmq.po.User;
import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-12-28 22:33
 */
@RestController
@RequestMapping("/rocket/mq/producer")
public class RocketMqProducerController {

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @PostMapping("/msg")
    public void sendMsg() {
        for (int i = 0; i < 10; i++) {
            try {
                SendResult sendResult = defaultMQProducer.send(new Message("user_topic", "white", JSON.toJSONBytes(User.builder()
                        .email(String.format("choleece@16%d", i)).name(String.format("choleece%d", i)).build())));

                System.out.println(String.format("消息id: %s, 发送状态: %s", sendResult.getMsgId(), sendResult.getSendStatus()));
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/msg/trans")
    public void sendTransMsg() {

    }

    @PostMapping("/msg/order")
    public void sendOrderMsg() throws Exception {
        for (int i = 0; i < 10; i++) {
            defaultMQProducer.send(new Message("user_topic", "white", JSON.toJSONBytes(User.builder()
                    .email(String.format("choleece@16%d", i)).name(String.format("choleece%d", i)).build())), new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    int index = ((Integer) arg) % mqs.size();
                    return mqs.get(index);
                }
            }, i);
        }
    }
}
