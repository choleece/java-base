package cn.choleece.base.collection.queue.demo.demo1;

import java.util.concurrent.DelayQueue;

public class Teacher implements Runnable {

    private DelayQueue<Student> students;

    public Teacher(DelayQueue<Student> students) {
        this.students = students;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                students.take().run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
