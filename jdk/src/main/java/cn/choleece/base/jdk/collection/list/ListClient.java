package cn.choleece.base.jdk.collection.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-05-08 00:00
 **/
public class ListClient {

    public static void main(String[] args) {

        // 如果不指定大小，那么array涉及多次扩容，速度远小于linked list 的add
        List list = new ArrayList(200001);
        // linked list添加的速度很快，优于arraylist，且不省空间，但是用for遍历很慢, 和array list相差甚远; foreach 两者差不多，array list 略胜一筹；iterator两者相当，linked list略胜一筹；
        // 总结一哈，iterator 性能最好，foreach其次，for对于数组友好(数组的for 和 iterator速度相当)
        List list1 = new LinkedList<>();

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            list.add(i);
        }
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++) {
            list1.add(i);
        }
        long start3 = System.currentTimeMillis();

        for (int i = 0; i < 200000; i++) {
            list.get(i);
        }
        long start4 = System.currentTimeMillis();
        for (int i = 0; i < 20000; i++) {
            list1.get(i);
        }
        long start5 = System.currentTimeMillis();
        for (Object o : list) {
            o.toString();
        }
        long start6 = System.currentTimeMillis();
        for (Object o : list1) {
            o.toString();
        }
        long start7 = System.currentTimeMillis();
        Iterator arrayIterator = list.iterator();
        while (arrayIterator.hasNext()) {
            arrayIterator.next();
        }
        long start8 = System.currentTimeMillis();
        Iterator linkedIterator = list1.iterator();
        while (linkedIterator.hasNext()) {
            linkedIterator.next();
        }
        long start9 = System.currentTimeMillis();

        System.out.println("add 200000 array list: " + (start2 - start1));
        System.out.println("add 200000 linked list: " + (start3 - start2));
        System.out.println("for array list: " + (start4 - start3));
        System.out.println("for linked list: " + (start5 - start4));
        System.out.println("foreach array list: " + (start6 - start5));
        System.out.println("foreach linked list: " + (start7 - start6));
        System.out.println("iterator array list: " + (start8 - start7));
        System.out.println("iterator linked list: " + (start9 - start8));

    }

}
