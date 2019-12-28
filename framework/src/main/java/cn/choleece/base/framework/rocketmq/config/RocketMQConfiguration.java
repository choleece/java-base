package cn.choleece.base.framework.rocketmq.config;

import cn.choleece.base.framework.rocketmq.po.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @description: rocket mq 配置文件
 * @author: choleece
 * @time: 2019-12-28 21:37
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@Slf4j
public class RocketMQConfiguration {

    @Autowired
    private RocketMQProperties mqProperties;

    /**
     * 事件监听
     */
    @Autowired
    private ApplicationEventPublisher publisher;

    @PostConstruct
    public void init() {
        System.out.println(mqProperties.toString());
    }

    /**
     * 创建普通消费者实例
     * @return
     */
    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(mqProperties.getProducerGroupName());
        defaultMQProducer.setNamesrvAddr(mqProperties.getNamesrvAddr());
        defaultMQProducer.setInstanceName(mqProperties.getProducerInstanceName());
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(10);
        defaultMQProducer.start();

        log.info("rocket producer is start...");
        return defaultMQProducer;
    }

    /**
     * 创建支持消息事务发送的实例
     * @return
     * @throws MQClientException
     */
    @Bean
    public TransactionMQProducer transactionMQProducer() throws MQClientException {
        TransactionMQProducer transMqProducer = new TransactionMQProducer(mqProperties.getTransactionProducerGroupName());
        transMqProducer.setNamesrvAddr(mqProperties.getNamesrvAddr());
        transMqProducer.setInstanceName(mqProperties.getProducerTransInstanceName());
        transMqProducer.setRetryTimesWhenSendAsyncFailed(10);

        // 4.x建议自己设置一个线程池
        transMqProducer.setExecutorService(Executors.newFixedThreadPool(10));

        transMqProducer.start();

        log.info("rocket transaction producer is start...");
        return transMqProducer;
    }


    @Bean
    public DefaultMQPushConsumer pushConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(mqProperties.getConsumerGroupName());
        consumer.setNamesrvAddr(mqProperties.getNamesrvAddr());
        consumer.setInstanceName(mqProperties.getConsumerInstanceName());

        // 判断是否是广播模式
        if (mqProperties.getConsumerBroadcasting()) {
            consumer.setMessageModel(MessageModel.BROADCASTING);
        }

        // 设置批量消费
        consumer.setConsumeMessageBatchMaxSize((mqProperties.getConsumerBatchMaxSize() == null
                || mqProperties.getConsumerBatchMaxSize() <= 0) ? 1 : mqProperties.getConsumerBatchMaxSize());

        List<String> subscribeList = mqProperties.getSubscribe();
        for (String subscribe : subscribeList) {
            String[] subscribes = subscribe.split(":");
            consumer.subscribe(subscribes[0], subscribes[1]);
        }

        if (mqProperties.getEnableOrderConsumer()) {
            orderConsume(consumer);
        } else {
            multiConsume(consumer);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    consumer.start();

                    log.info("rocket mq consumer server is starting....");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return consumer;
    }

    /**
     * 顺序消费
     * @param consumer
     */
    public void orderConsume(DefaultMQPushConsumer consumer) {
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                try {
                    context.setAutoCommit(true);
                    msgs = filterMsg(msgs);
                    if (msgs.size() == 0) {
                        return ConsumeOrderlyStatus.SUCCESS;
                    }

                    publisher.publishEvent(new MessageEvent(msgs, consumer));
                } catch (Exception e) {
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
    }

    /**
     * 并行消费
     * @param consumer
     */
    public void multiConsume(DefaultMQPushConsumer consumer) {
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                try {
                    msgs = filterMsg(msgs);
                    if (msgs.size() == 0) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }

                    publisher.publishEvent(new MessageEvent(msgs, consumer));
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
    }

    /**
     * 是否是第一次订阅
     */
    private static Boolean IS_FIRST_SUB = true;

    /**
     * 系统启动事件
     */
    private static final Long START_TIME = System.currentTimeMillis();

    /**
     * 消息过滤
     * @param msgs
     * @return
     */
    private List<MessageExt> filterMsg(List<MessageExt> msgs) {
        if (IS_FIRST_SUB && !mqProperties.getEnableHistoryConsumer()) {
            msgs.stream().filter(item -> START_TIME - item.getBornTimestamp() < 0)
                    .collect(Collectors.toList());
        }

        if (IS_FIRST_SUB && CollectionUtils.isEmpty(msgs)) {
            IS_FIRST_SUB = false;
        }

        return msgs;
    }
}
