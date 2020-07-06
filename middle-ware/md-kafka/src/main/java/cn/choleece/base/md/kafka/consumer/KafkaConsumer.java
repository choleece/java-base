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
public class KafkaConsumer {

    @KafkaListener(topics = "test-topic")
    public void processMessage(SampleMessage message) {
        System.out.println("Received sample message [" + message + "]");
    }
}
