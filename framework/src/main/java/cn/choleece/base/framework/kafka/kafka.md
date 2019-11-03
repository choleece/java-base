# Kafka
1. broker 可以简单理解为一个kafka服务，一个独立的kafka服务阔以称为一个broker, broker是集群的一部分，一个分区会被分配到多个broker ，这里就出现了分区副本(replica)
2. topic 消息主题 
3. partition 分区，一个topic 的数据可以放到多个partition 里，单个partition的消息是有序的，FIFO，由于一个topic的消息是放到多个partition 里，所以不能保证topic 消息整体有序（除非一个topic一个partition）
4. producer 往topic 里写数据
5. consumer 消费topic 里的数据（一般的消息系统有push 或 pull 两种格式， kafka采用producer push消息到topic, consumer 从topic pull消息） 
6. ACK (request.required.acks有三个取值)
```
    0: producer 只管往topic发消息，不管返回，这种消息有丢失风险
    1: producer 往topic发消息，会等到leader返回确认收到消息，这里也有丢失消息的风险（比如leader挂了，但是消息还没来得及同步到follower）
   -1: producer 往 topic发消息，leader会等到所有的follower收到消息副本后，返回给leader,然后leader才会返回，这种消息不会丢失
```
7. 数据传输的事物定义有哪三种
```
    最多传一次：消息不会被重复传送，但有可能一次也不出传送
    最少传一次：消息不会被漏传，最少传送一次，但是有可能会多传，导致重复传送
    精确的一次（Exactly once）: 不会漏传输也不会重复传输,每个消息都传输被一次而且仅仅被传输一次，这是大家所期望的
```
8. offset 偏移量 每个分区都有一个偏移量，是一个递增的值，消费者把最后读取消息的偏移量保存在 kafka 或zookeeper等介质里，这样消费者重启或消费者关闭，消息读取位置不会丢失
9. consumer group 消费者组 会有一个或多个消费者共同订阅同一个主题，群组保证消息只被群组里的一个消费者消费
10. 保留消息 （一定期限内）保留消息是kafka的一个重要特性，kafka 消息保留策略有保留一段时间（比如7天），或者保留消息达到一定大小的字节数（比如1GB），当消息数量达到这些上限时，
旧消息就会过期并被删除，所以在任何时刻，可哟个消息的总量都不会超过参数指定的大小），具体可查看配置文件里的Log Retention Policy
11. 使用场景 
```
    1. 活动追踪，记录用户操作行为，比如浏览时，点击等操作
    2. 传递消息，纯消息队列功能
    3. 度量指标和日志记录 收集应用程序和系统度量指标以及日志，因为kafka可以支持多生产者
    4. 提交日志 比如数据库多更新日志发布到kafka上，应用程序通过监控事件流来接收数据库的实时更新
    5. 流处理
```