package cn.choleece.base.concurrent.procon;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by choleece on 2019/6/22.
 */
public class BlockQueueModel implements Model {

    final BlockingQueue<Task> queue;

    public BlockQueueModel(int cap) {
        this.queue = new LinkedBlockingDeque<>(cap);
    }

    final AtomicInteger increTaskNo = new AtomicInteger(0);

    class ConsumerImpl extends AbstractConsumer implements Consumer, Runnable {
        @Override
        public void consume() throws InterruptedException {
            Task task = queue.take();
            // 固定时间范围的消费，模拟相对稳定的服务器处理过程
            Thread.sleep(500 + (long) (Math.random() * 500));
            System.out.println("consume: " + task.no);
        }
    }

    class ProducerImpl extends AbstractProducer implements Producer, Runnable {
        @Override
        public void produce() throws InterruptedException {
            // 不定期生产，模拟随机的用户请求
            Thread.sleep((long) (Math.random() * 1000));
            Task task = new Task(increTaskNo.getAndIncrement());
            System.out.println("produce: " + task.no);
            queue.put(task);

            System.out.println(queue.size());
        }
    }

    @Override
    public Runnable newConsumer() {
        return new ConsumerImpl();
    }

    @Override
    public Runnable newProducer() {
        return new ProducerImpl();
    }

    public static void main(String[] args) {
        Model model = new BlockQueueModel(3);

        for (int i = 0; i < 2; i++) {
//            new Thread(model.newConsumer()).start();
        }
        for (int i = 0; i < 10; i++) {
            new Thread(model.newProducer()).start();
        }
    }
}
