package cn.choleece.base.jdk.collection.queue.demo.demo1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Student implements Runnable, Delayed {

    private String name;

    private long workTime;

    private long submitTime;

    private boolean isForce = false;

    private CountDownLatch countDownLatch;

    public Student() {
    }

    public Student(String name, long workTime, CountDownLatch countDownLatch) {
        this.name = name;
        this.workTime = workTime;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null || !(o instanceof Student)) {
            return 1;
        }
        if (o == this) {
            return 1;
        }
        Student s = (Student)o;
        if (this.workTime > s.workTime) {
            return 1;
        } else if (this.workTime == s.workTime) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public void run() {
        if (isForce) {
            System.out.println("name: " + name + " 交卷，希望用时" + workTime + "实际用时120min");
        } else {
            System.out.println("name: " + name + " 交卷，希望用时" + workTime + "实际用时" + workTime + "min");
        }
        countDownLatch.countDown();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(workTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }
}
