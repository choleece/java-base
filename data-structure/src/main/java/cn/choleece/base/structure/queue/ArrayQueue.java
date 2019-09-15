package cn.choleece.base.structure.queue;

/**
 * @author choleece
 * @Description: 队列 FIFO 这种方式有一种问题，会造成浪费空间浪费，0->1->2->3 依次递增队时候，后边队空间会浪费掉
 * @Date 2019-09-15 16:28
 **/
public class ArrayQueue {

    private String[] items;

    private int capacity;

    private int head = 0;

    private int tail = 0;

    public ArrayQueue(int capacity) {
        this.capacity = capacity;
        this.items = new String[capacity];
    }

    public boolean enqueue(String s) {
        // the queue is full
        if (tail == capacity) {
            return false;
        }

        // 从尾部插入队列
        items[tail++] = s;
        return true;
    }

    public String dequeue() {

        // the queue is empty
        if (head == tail) {
            return null;
        }

        // 从头开始出队
        return items[head++];
    }

    public void print() {
        for (int i = 0; i < items.length; i++) {
            System.out.print(items[i] + " ");
        }
    }
}
