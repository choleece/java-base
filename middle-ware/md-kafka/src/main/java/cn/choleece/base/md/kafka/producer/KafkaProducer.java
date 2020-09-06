package cn.choleece.base.md.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author choleece
 * @Description: kafka 消息生产者
 * @Date 2019-11-03 21:43
 **/
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<Object, String> kafkaTemplate;

    public void send(String topic, String message) {
        // 什么都不做，是异步提交
        ListenableFuture<SendResult<Object, String>> future = kafkaTemplate.send(topic, message);

        // 在这里调用future.get()，由于future是阻塞，这里为同步提交
        try {
            System.out.println(future.get().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
