package cn.choleece.base.collection.queue.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author choleece
 * @Description: 测试阻塞队列
 * @Date 2019-09-16 21:51
 **/
public class TestBlockingQueue {

    public static void main(String[] args) {
//        testAdd();

//        testOffer();

//        testRemove();

//        testPoll();

//        testElement();

        testPeek();
    }

    public static void testAdd() {
        BlockingQueue queue = new ArrayBlockingQueue(2);

        /**
         *
         * 此时会报错，因为add操作已经超过了array的容量，会抛出IllegalStateException Queue full, 将i调整为2就不会报错
         * 底层调用的offer(e)
         *
         *     public boolean add(E e) {
         *         if (offer(e))
         *             return true;
         *         else
         *             throw new IllegalStateException("Queue full");
         *     }
         */
        for (int i = 0; i < 10; i++) {
            queue.add(i);
        }
    }

    /**
     * 此时不会抛出异常，当超出存储范围后，offer 返回false
     */
    public static void testOffer() {
        BlockingQueue queue = new ArrayBlockingQueue(2);

        for (int i = 0; i < 10; i++) {
            /**
             *    public boolean offer(E e) {
             *         // 会去检查e是否为null，如果为null 会抛出异常，因为poll，peek操作，如果为空，会直接返回空，如果队列中当数据允许存放空，则对与poll、peek操作不好判断错误与否
             *         checkNotNull(e);
             *         final ReentrantLock lock = this.lock;
             *         lock.lock();
             *         try {
             *             if (count == items.length)
             *                 return false;
             *             else {
             *                 enqueue(e);
             *                 return true;
             *             }
             *         } finally {
             *             lock.unlock();
             *         }
             *     }
             */
            queue.offer(i);

            /**
             * 这里会抛出NPE
             */
            queue.offer(null);
        }
    }

    public static void testRemove() {
        BlockingQueue queue = new ArrayBlockingQueue(2);
        queue.offer(1);
        queue.offer(2);

        queue.remove();
        queue.remove();

        /**
         * 这里会抛出no such element exception
         */
        queue.remove();
    }

    public static void testPoll() {
        BlockingQueue queue = new ArrayBlockingQueue(2);
        queue.offer(1);
        queue.offer(2);

        queue.poll();
        queue.poll();

        /**
         * 这里会返回为空，不会抛出no such element exception
         */
        queue.poll();
    }

    public static void testElement() {
        BlockingQueue queue = new ArrayBlockingQueue(2);

        /**
         * 这里会抛出异常，no such element exception,因为我们这里队列还为空，没有队列头供返回
         */
        queue.element();
    }

    public static void testPeek() {
        BlockingQueue queue = new ArrayBlockingQueue(2);

        /**
         * 这里不会抛出异常，会返回为空
         */
        queue.peek();
    }

}
