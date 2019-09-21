package cn.choleece.base.concurrent.lock.reentrantlock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-09-20 13:44
 */
public class BoundedBuffer {

    final Lock lock = new ReentrantLock();

    final Condition notFullCondition = lock.newCondition();

    final Condition notEmptyCondition = lock.newCondition();

    final Object[] items = new Object[100];

    int count, putptr, takeptr;

    public void put(Object x) throws InterruptedException {
        lock.lock();

        try {
            if (count == items.length) {
                /**
                 * 此时队列已满，需要等待
                 */
                notFullCondition.await();
            }
            items[putptr++] = x;
            count++;
            if (putptr == items.length) {
                putptr = 0;
            }
            // 通知队列现在不是空的了
            notEmptyCondition.notify();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();

        try {
            if (count == 0) {
                /**
                 * 队列为空，等待，直到队列 not empty，才能继续消费
                 */
                notEmptyCondition.await();
            }
            Object x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            notFullCondition.notify();
            count--;
            return x;
        } finally {
            lock.unlock();
        }
    }

}
