package cn.choleece.base.jdk.collection.queue.demo.demo1;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;

public class EndExam extends Student {
    private DelayQueue<Student> students;

    private CountDownLatch countDownLatch;

    private Thread teacherThread;

    public EndExam(long workTime, DelayQueue<Student> students, CountDownLatch countDownLatch, Thread teacherThread) {
        super("强制交卷", workTime, countDownLatch);
        this.students = students;
        this.countDownLatch = countDownLatch;
        this.teacherThread = teacherThread;
    }

    @Override
    public void run() {
        teacherThread.interrupt();
        Student tmpStudent;

        for (Iterator<Student> iterator = students.iterator(); iterator.hasNext();) {
            tmpStudent = iterator.next();
            tmpStudent.setForce(true);
            tmpStudent.run();
        }
        countDownLatch.countDown();
    }
}
