package cn.choleece.base.md.kafka.consumer;

import cn.choleece.base.md.kafka.message.SampleMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author choleece
 * @Description: kafka 消费者
 * @Date 2020-07-05 16:21
 **/
@Component
public class KafkaConsumerTwo {

    @KafkaListener(topics = "test-group", groupId = "group-2")
    public void processMessage(String message) {
        System.out.println("Received sample message [" + message + "]  I am the consumer 2");
    }
}
