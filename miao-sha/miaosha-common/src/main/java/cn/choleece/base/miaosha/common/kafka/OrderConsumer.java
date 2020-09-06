package cn.choleece.base.miaosha.common.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author choleece
 * @Description: miao sha order kafka consumer
 * @Date 2020-09-06 16:37
 **/
@Component
public class OrderConsumer {

    @KafkaListener(topics = "miaosha")
    public void processMessage(String message) {
        System.out.println("Received sample message [" + message + "] I am the consumer 1");
    }
}
