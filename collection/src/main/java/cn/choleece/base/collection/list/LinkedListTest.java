package cn.choleece.base.collection.list;

import java.util.LinkedList;
import java.util.List;

/**
 * LinkedList通过双向链表来存储数据，它继承了AbstractList类，实现了List & Deque接口
 * 其可以存储重复的数据，允许插入等操作，但是它不是一个线程安全的，它的删除插入等操作比ArrayList快，因为不涉及到数据的移动，
 * 同时其随机取的速度慢于ArrayList，它可以被用作为list, stack, queue
 */
public class LinkedListTest {

    /**
     * LinkedList节点，是一个双向链表，分别指向前、后
     *     private static class Node<E> {
     *         E item;
     *         Node<E> next;
     *         Node<E> prev;
     *
     *         Node(Node<E> prev, E element, Node<E> next) {
     *             this.item = element;
     *             this.next = next;
     *             this.prev = prev;
     *         }
     *     }
     *
     */

    /**
     * 测试添加，默认用尾插法
     */
    public static void testAdd() {
        List<String> list = new LinkedList<>();
        list.add("bing");

        /**
         * 默认直接采用尾插入
         */
        System.out.println("------list.add()------");
        list.forEach(s -> System.out.println(s));

        /**
         * 指定位置插入 通过指定的index,判断是从前往后遍历插入 or 直接在尾部插入
         * 如果不是采用尾插法(只有在index == size的时候，才默认是这种方式),其他情况都是从前开始遍历，找到相应的位置，然后开始插入
         * 找到"相应位置"，有两种方式，一个是从头往后找，一个是从后往前找，会根据index 与 size >> 1的大小比较，判断是从头开始找还是从尾部开始找
         */
        list.add(1, "zheng");

        System.out.println("------list.add(int index, E e)------");
        list.forEach(s -> System.out.println(s));
    }

    public static void main(String[] args) {
        testAdd();
    }

}
