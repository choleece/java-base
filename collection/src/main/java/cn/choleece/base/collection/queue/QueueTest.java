package cn.choleece.base.collection.queue;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * 队列知识：队列，阻塞队列(blocking queue，有很多的实现，LinkedBlockingQueue, ArrayBlockingQueue)，延迟队列（DelayQueue, 存放的内容必须继承Delayed接口， DelayQueue本身实现blocking queue）
 *
 * ## Queue的常用方法
 *
 * 1. boolean add(E e);
 *    在队尾部插入指定的元素，如果有异常，会对异常进行抛出
 *
 * 2. boolean offer(E e);
 *    在队尾部插入指定的元素，不抛异常
 *
 * 3. E remove();
 *    删除队头元素，同时返回该元素，如果队列位空，则抛出异常
 *
 * 4. E poll();
 *    删除队头元素，同时返回该元素，如果队列位空，则返回空
 *
 * 5. E element();
 *    返回队头元素，不删除，如果队列位空，则抛出异常
 *
 * 6. E peek();
 *    返回队头元素，不删除，如果队列位空，则返回空
 * Queue是一种先进先出的数据结构，它保证了First in First out的顺序
 *
 * 7. put(E e) 添加一个元素  如果队列满，则阻塞
 *
 * 8. take() 移除并返回队列头部的元素  如果队列为空，则阻塞
 *
 * 队列不允许添加空元素null，因为不好做判断，像element, peek，会针对队列情况，返回null，此时就不好判断，是确实因为有空元素还是因为队列有问题
 *
 * @author choleece
 * @date 2019/6/16
 */
public class QueueTest {

    public static void testPriorityQueue() {

        System.out.println("test priority queue");

        Queue<String> queue = new PriorityQueue<>();
        queue.add("choleece");
        queue.offer("bing");
        System.out.println(queue);

        queue.add("zheng");
        queue.add("li");
        System.out.println(queue);

        queue.remove();
        queue.poll();
        System.out.println(queue);

        System.out.println("element: " + queue.element());
        System.out.println("peak: " + queue.peek());
        System.out.println(queue);
    }

    public static void testLinkedBlockingQueue() {
        Queue queue = new LinkedBlockingQueue();
    }

    public static void main(String[] args) {
        testPriorityQueue();
    }

}
