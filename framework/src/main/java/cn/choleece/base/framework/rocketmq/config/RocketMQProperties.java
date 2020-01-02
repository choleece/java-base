package cn.choleece.base.framework.rocketmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedList;
import java.util.List;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-12-28 21:38
 */
@Data
@ConfigurationProperties(prefix = "choleece.rocket.mq")
public class RocketMQProperties {

    private String namesrvAddr;

    private String producerGroupName;
    private String consumerGroupName;
    private String transactionProducerGroupName;
    private String producerInstanceName;
    private String consumerInstanceName;
    private String producerTransInstanceName;
    private Integer consumerBatchMaxSize;
    private Boolean consumerBroadcasting;
    private List<String> subscribe = new LinkedList<>();
    private Boolean enableHistoryConsumer;
    private Boolean enableOrderConsumer;
}
