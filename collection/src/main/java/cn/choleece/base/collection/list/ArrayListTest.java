package cn.choleece.base.collection.list;

import java.util.ArrayList;
import java.util.List;

/**
 * ArrayList实现list接口，它利用一个动态的数组存储重复的、不同的类型数据，ArrayList保持与插入顺序一直，但是不保证线程安全，ArrayList里的数据可以随机存取（因为采用数组实现）
 */
public class ArrayListTest {

    /**
     * ArrayList 提供三类构造函数，第一个无参数构造，ArrayList会初始化一个空对象{}，即 DEFAULTCAPACITY_EMPTY_ELEMENTDATA，如下
     * public ArrayList() {
     *     this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
     * }
     * @param
     */
    public static void testNoArgsConsArrayList() {
        List<String> list = new ArrayList<String>();
        list.add("choleece");
        list.add("bing");

        list.forEach(name -> {System.out.println(name);});
        System.out.println(list.size());
    }

    /**
     * 提供一个初始化大小的数组，ArrayList会根据提供的大小初始化一个数组
     * public ArrayList(int initialCapacity) {
     *     if (initialCapacity > 0) {
     *         this.elementData = new Object[initialCapacity];
     *     } else if (initialCapacity == 0) {
     *         this.elementData = EMPTY_ELEMENTDATA;
     *     } else {
     *         throw new IllegalArgumentException("Illegal Capacity: "+
     *                                            initialCapacity);
     *     }
     * }
     */
    public static void testInitSizeArrayList() {
        List<String> list = new ArrayList<String>(2);
        list.add("choleece");
        list.add("bing");

        list.forEach(name -> {System.out.println(name);});
        System.out.println(list.size());
    }

    /**
     * 提供一个初始化集合的数据，ArrayList会根据给定的集合进行初始化
     * public ArrayList(Collection<? extends E> c) {
     *     elementData = c.toArray();
     *     if ((size = elementData.length) != 0) {
     *         // c.toArray might (incorrectly) not return Object[] (see 6260652)
     *         if (elementData.getClass() != Object[].class)
     *             elementData = Arrays.copyOf(elementData, size, Object[].class);
     *     } else {
     *         // replace with empty array.
     *         this.elementData = EMPTY_ELEMENTDATA;
     *     }
     * }
     */
    public static void testCollectionArgArrayList() {
        List<String> initList = new ArrayList<>();
        initList.add("choleece");
        initList.add("bing");

        List<String> list = new ArrayList<>(initList);
        list.forEach(name -> {System.out.println(name);});
        System.out.println(list.size());
    }

    /**
     * ArrayList添加 添加过程可能会涉及到扩容，容量变为 oldCapacity + (oldCapacity >> 1)，如果没有指定capacity, default_capacity为10
     */
    public static void testArrayListAdd() {
        List<String> list = new ArrayList<String>();
        list.add("choleece");

        list.add(1, "bing");

        list.addAll(new ArrayList<String>(){{
            add("bing");
            add("bing");
        }});

        list.addAll(1, new ArrayList<String>(){{
            add("choleece");
            add("choleece");
        }});

        list.forEach(name -> {System.out.println(name);});
        System.out.println(list.size());
    }

    /**
     * system.copyarray
     * src:源数组；
     * srcPos:源数组要复制的起始位置；
     * dest:目的数组；
     * destPos:目的数组放置的起始位置；
     * length:复制的长度。
     * 注意：src and dest都必须是同类型或者可以进行转换类型的数组
     * 此方法在ArrayList大量使用，是JDK的一个Native方法
     */
    public static void testSystemCopyArray() {
        String[] src = {"1", "2", "3", "4", "5"};
        String[] dest = new String[5];

        System.arraycopy(src, 0, dest, 0, 5);

        for (String s : dest) {
            System.out.println(s);
        }
    }

    /**
     * 通过下标获取指定index的值
     */
    public static void testGet() {
        List<String> list = new ArrayList<>();
        list.add("choleece");
        list.add("bing");

        System.out.println(list.get(1));
    }

    /**
     * 替换给定下标的值
     */
    public static void testSet() {
        List<String> list = new ArrayList<>();
        list.add("choleece");
        list.add("bing");

        list.set(1, "yin");

        System.out.println(list.get(1));
    }

    public static void testMove() {
        List<String> list = new ArrayList<>();
        list.add("choleece");
        list.add("bing");
        list.add("yin");
        list.add("zheng");

        System.out.println("-----move前-----");
        list.forEach(s -> System.out.println(s));

        // 清除指定下标
        list.remove(0);

        System.out.println("-----move index 0-----");
        list.forEach(s -> System.out.println(s));

        // 清除指定的内容
        list.remove("bing");

        System.out.println("-----move bing-----");
        list.forEach(s -> System.out.println(s));

        list.removeAll(new ArrayList<String>() {
            {
                add("zheng");
            }
        });

        System.out.println("-----move zheng collection-----");
        list.forEach(s -> System.out.println(s));

    }

    public static void main(String[] args) {
        testNoArgsConsArrayList();
        testInitSizeArrayList();
        testCollectionArgArrayList();
        testArrayListAdd();
        testSystemCopyArray();

        testGet();

        testSet();

        testMove();
    }

}
