package cn.choleece.base.md.kafka.producer;

import cn.choleece.base.md.kafka.message.SampleMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author choleece
 * @Description: kafka 消息生产者
 * @Date 2019-11-03 21:43
 **/
@Component
public class KafkaProducer {

    private final KafkaTemplate<Object, SampleMessage> kafkaTemplate;

    KafkaProducer(KafkaTemplate<Object, SampleMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(SampleMessage message) {
        kafkaTemplate.send("test-group", message);
    }


}
