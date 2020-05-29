package cn.choleece.base.jdk.collection.queue;

import lombok.Data;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author choleece
 * @Description: 优先队列的使用
 * @Date 2020-05-29 23:38
 **/
public class PriorityQueueClient {

    public static void main(String[] args) {

        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };

        Queue<Integer> queue = new PriorityQueue<>(comparator);

        queue.offer(6);
        queue.offer(5);
        queue.offer(10);

        System.out.println(queue.peek());

        Student student1 = new Student("choleece", 5);
        Student student2 = new Student("choleece", 1);
        Student student3 = new Student("choleece", 10);

        Queue<Student> queue1 = new PriorityQueue<>(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o2.getAge() - o1.getAge();
            }
        });

        queue1.offer(student1);
        queue1.offer(student2);
        queue1.offer(student3);

        System.out.println(queue1.peek());
    }
}

@Data
class Student {
    private String name;

    private Integer age;

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
