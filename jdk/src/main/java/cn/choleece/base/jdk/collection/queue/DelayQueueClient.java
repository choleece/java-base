package cn.choleece.base.jdk.collection.queue;

import lombok.Data;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: https://biteeniu.github.io/java/java-delay-queue/
 * @Date 2020-05-24 23:51
 **/
public class DelayQueueClient {

    public static void main(String[] args) {

        DelayQueue<MessageDelayed> queue = new DelayQueue<>();

        new Thread(new ConsumerThread(queue)).start();

        test1(queue);
    }

    /**
     * 每隔1秒打印一次消息
     * @param queue
     */
    public static void test1(DelayQueue<MessageDelayed> queue) {
        for (int i = 1; i <= 5; i++) {
            queue.put(new MessageDelayed("测试消息-" + i, i * 1000L));
        }
    }

}

@Data
class MessageDelayed implements Delayed {

    private String msg;

    private long ttl;

    public MessageDelayed(String msg, long ttl) {
        this.msg = msg;
        this.ttl = System.currentTimeMillis() + ttl;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        // 计算延迟任务还剩多少时间
        return unit.convert(ttl - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        // 比较两个队列的优先级，根据延迟时间，越短优先级越大
        return (int)(this.getDelay(TimeUnit.MICROSECONDS) - o.getDelay(TimeUnit.MICROSECONDS));
    }
}

/**
 * 消费线程
 */
@Data
class ConsumerThread implements Runnable {

    private DelayQueue<MessageDelayed> queue;

    public ConsumerThread(DelayQueue<MessageDelayed> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 从队列头部获取已过期消息，如果队列为空或者无过期消息，线程会阻塞
                // 阻塞原理为底层用到阻塞队列，这里的队列为PriorityQueue
                MessageDelayed delayed = queue.take();
                System.out.println(new Date() + " 获取到消息了.." + delayed.getMsg());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
