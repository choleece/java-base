package cn.choleece.base.structure.queue;

/**
 * @author choleece
 * @Description: 循环队列 重点在与对列满 & 队列空队条件
 * @Date 2019-09-15 17:25
 **/
public class CircleQueue {

    private String[] items;

    private int capacity;

    private int head = 0;

    private int tail = 0;

    public CircleQueue(int capacity) {
        this.capacity = capacity;
        this.items = new String[capacity];
    }

    /**
     * 入队
     * @param s
     * @return
     */
    public boolean enqueue(String s) {
        // the queue is full
        boolean isQueueFull = (tail + 1) % capacity == head;
        if (isQueueFull) {
            return false;
        }

        items[tail] = s;

        tail = (tail + 1) % capacity;
        return true;
    }

    /**
     * 出队
     * @return
     */
    public String dequeue() {
        // the queue is empty
        if (head == tail) {
            return null;
        }

        String val = items[head];

        head = (head + 1) % capacity;

        return val;
    }
}
