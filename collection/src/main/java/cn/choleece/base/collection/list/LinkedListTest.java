package cn.choleece.base.collection.list;

import java.util.ArrayList;
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

        list.addAll(new ArrayList<String>() {
            {
                add("chao");
                add("li");
            }
        });

        /**
         * 底层实现是针对c进行遍历插入，其实是一种特殊的list.addAll(index, collection)，此时index为size
         */
        System.out.println("------list.addAll(Collection c)------");
        list.forEach(s -> System.out.println(s));

        list.addAll(1, new ArrayList<String>() {
            {
                add("y");
                add("t");
            }
        });

        /**
         * 底层实现是,在指定的位置index,针对c进行遍历插入
         */
        System.out.println("------list.addAll(int index, Collection c)------");
        for (String s : list) {
            System.out.println(s);
        }
        list.forEach(s -> System.out.println(s));
    }

    public static void testGetAndSet() {
        List<String> list = new LinkedList<>();
        list.add("bing");

        System.out.println("------init------");
        list.forEach(s -> System.out.println(s));

        System.out.println("------get------");
        /**
         * get操作，会先找到index对应的内容，然后返回，找到index跟上边添加add(int index, Node n)逻辑一直
         */
        System.out.println(list.get(0));

        /**
         * set操作，与get同理，只是会替换内容
         */
        list.set(0, "choleece");
        System.out.println("------set------");
        list.forEach(s -> System.out.println(s));

    }

    public static void testRemove() {
        List<String> list = new LinkedList<>();
        list.add("bing");
        list.add("choleece");
        list.add("zheng");
        list.add("chao");

        System.out.println("------init------");
        System.out.println(list);

        /**
         * 都是找到对应index的node，然后对其进行unlink操作
         */
        list.remove(0);

        System.out.println("------remove(int index)------");
        System.out.println(list);

        /**
         * 根据值找到对应node，然后对其进行unlink操作
         */
        list.remove("bing");

        System.out.println("------remove(Object o)------");
        System.out.println(list);

        /**
         * 与remove(Object o)同理，对collection进行遍历,循环调用remove
         */
        list.removeAll(new ArrayList<String>(){{
            add("zheng");
        }});

        System.out.println("------removeAll(Collection c)------");
        System.out.println(list);

    }

    public static void main(String[] args) {
//        testAdd();
//        testGetAndSet();
        testRemove();
    }

}
