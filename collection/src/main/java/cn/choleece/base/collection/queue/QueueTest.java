package cn.choleece.base.collection.queue;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Queue底层是一个由数组实现的数据结构，它保证了First in First out的顺序
 * Created by choleece on 2019/6/16.
 */
public class QueueTest {

    public static void main(String[] args) {
        Queue<String> queue = new PriorityQueue<>();
        queue.add("choleece");
        queue.offer("bing");
        queue.add("chao");

        System.out.println(queue);
    }

}
