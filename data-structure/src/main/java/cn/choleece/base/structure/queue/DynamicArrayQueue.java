package cn.choleece.base.structure.queue;

/**
 * @author choleece
 * @Description: 当tail达到capacity后，然后将数据进行压缩
 * @Date 2019-09-15 17:01
 **/
public class DynamicArrayQueue {

    private String[] items;

    private int capacity;

    private int head = 0;

    private int tail = 0;

    public DynamicArrayQueue(int capacity) {
        this.capacity = capacity;
        this.items = new String[capacity];
    }

    /**
     * 当tail 到达队顶队时候，此时移动数据，对数据进行压缩
     * @param s
     * @return
     */
    public boolean enqueue(String s) {
        if (tail == capacity) {
            // the queue is full
            if (head == 0) {
                return false;
            }

            // move item to zero index
            for (int i = head; i < tail ; i++) {
                items[i - head] = items[i];
            }

            tail -= head;
            head = 0;
        }
        items[tail++] = s;
        return true;
    }

    public String dequeue() {
        // the queue is empty
        if (head == capacity) {
            return null;
        }

        return items[head++];
    }

    public void print() {
        for (int i = 0; i < items.length; i++) {
            System.out.print(items[i] + " ");
        }
    }

    public static void main(String[] args) {
        DynamicArrayQueue queue = new DynamicArrayQueue(10);

        for (int i = 0; i < 10; i++) {
            queue.enqueue(i + "");
        }

        for (int i = 0; i < 5; i++) {
            queue.dequeue();
        }

        for (int i = 10; i < 15; i++) {
            queue.enqueue(i + "");
        }

        queue.print();

        System.out.println("tail: " + queue.tail);
        System.out.println("head: " + queue.head);
    }
}
