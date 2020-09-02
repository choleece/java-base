package cn.choleece.base.md.kafka.controller;

import cn.choleece.base.md.kafka.message.SampleMessage;
import cn.choleece.base.md.kafka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-07-06 21:25
 **/
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private KafkaProducer producer;

    @PostMapping("/")
    public String sendMessage() {
        try {

            producer.send(new SampleMessage(System.currentTimeMillis(), String.format("test message " + new Random().nextInt())).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
