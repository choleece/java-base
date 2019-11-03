package cn.choleece.base.framework.kafka.producer;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-03 21:43
 **/
public class KafkaProducer {

    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("acks", "all");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    Producer<String, String> producer = new KafkaProducer<>(props);



}
