package cn.choleece.base.collection.queue.demo.demo1;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;

/**
 * 延迟队列使用场景1
 * 模拟一个考试的日子，考试时间为120分钟，30分钟后才可交卷，当时间到了，或学生都交完卷了考试结束。
 * 考试时间为120分钟，30分钟后才可交卷，初始化考生完成试卷时间最小应为30分钟
 * 对于能够在120分钟内交卷的考生，如何实现这些考生交卷
 * 对于120分钟内没有完成考试的考生，在120分钟考试时间到后需要让他们强制交卷
 * 在所有的考生都交完卷后，需要将控制线程关闭
 */
public class Exam {

    public static void main(String[] args) throws InterruptedException {
        int studentNumber = 20;

        CountDownLatch countDownLatch = new CountDownLatch(studentNumber + 1);

        DelayQueue<Student> students = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            students.put(new Student("student" + (i + 1), 30 + random.nextInt(10), countDownLatch));
        }

        Thread teacherThread = new Thread(new Teacher(students));
        students.put(new EndExam(120, students, countDownLatch, teacherThread ));

        teacherThread.start();
        countDownLatch.await();

        System.out.println("考试完毕，考试交卷");
    }

}
