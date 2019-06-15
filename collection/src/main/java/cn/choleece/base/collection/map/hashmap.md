# 一篇文章，详解HashMap

## HashMap简介

HashMap是我们在Java中经常用到的K-V存储结构，它是一个非线程安全的类，并且它不保证数据插入的顺序，允许key & value都为空，不允许重复的key，它实现了AbstractHashMap，继承了Map。其底层数据结构由数组 + 链表 + 红黑树组成，下面，我将在这篇文章中详细介绍HashMap。

### 基本概念


**DEFAULT_INITIAL_CAPACITY** 默认数组初始大小 默认为16 1 << 4（**数组容量必须为2的幂**）
```
/**
* The default initial capacity - MUST be a power of two.
*/
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
```

**MAXIMUM_CAPACITY** 最大数组容量 1 << 30
```
/**
* The maximum capacity, used if a higher value is implicitly specified
* by either of the constructors with arguments.
* MUST be a power of two <= 1<<30.
*/
static final int MAXIMUM_CAPACITY = 1 << 30;
```

**DEFAULT_LOAD_FACTOR** 默认负载因子 0.75
```
/**
* The load factor used when none specified in constructor.
*/
static final float DEFAULT_LOAD_FACTOR = 0.75f;
```

**TREEIFY_THRESHOLD** 链表变成红黑树的临界值 8
```
/**
* The bin count threshold for using a tree rather than list for a
* bin.  Bins are converted to trees when adding an element to a
* bin with at least this many nodes. The value must be greater
* than 2 and should be at least 8 to mesh with assumptions in
* tree removal about conversion back to plain bins upon
* shrinkage.
*/
static final int TREEIFY_THRESHOLD = 8;
```

**UNTREEIFY_THRESHOLD** 红黑树退回链表的临界值 6
```
/**
* The smallest table capacity for which bins may be treeified.
* (Otherwise the table is resized if too many nodes in a bin.)
* Should be at least 4 * TREEIFY_THRESHOLD to avoid conflicts
* between resizing and treeification thresholds.
*/
static final int MIN_TREEIFY_CAPACITY = 64;
```

**Node<K,V>** HashMap数据存储的结构,k-v键值对
```
/**
 * Basic hash bin node, used for most entries.  (See below for
 * TreeNode subclass, and in LinkedHashMap for its Entry subclass.)
 */
// 此结构为一个单向链表结构，next指向下一个节点
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;

    Node(int hash, K key, V value, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final K getKey()        { return key; }
    public final V getValue()      { return value; }
    public final String toString() { return key + "=" + value; }

    public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    public final V setValue(V newValue) {
        V oldValue = value;
        value = newValue;
        return oldValue;
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Map.Entry) {
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;
            if (Objects.equals(key, e.getKey()) &&
                Objects.equals(value, e.getValue()))
                return true;
        }
        return false;
    }
}
```

**table** HashMap底层数组，用于存储数据，此数组在第一次使用的时候才进行初始化，其大小必须为2的幂(在上面也指出过),其会在**必要**的时候进行扩容
```
/**
 * The table, initialized on first use, and resized as
 * necessary. When allocated, length is always a power of two.
 * (We also tolerate length zero in some operations to allow
 * bootstrapping mechanics that are currently not needed.)
 */
transient Node<K,V>[] table;
```

**entrySet** HashMap存储的数据节点组成的set集合
```
/**
 * Holds cached entrySet(). Note that AbstractMap fields are used
 * for keySet() and values().
 */
transient Set<Map.Entry<K,V>> entrySet;
```

**size** HashMap中键值对存储的个数
```
/**
 * The number of key-value mappings contained in this map.
 */
transient int size;
```

**threshold** HashMap需要扩容的临界值
```
/**
 * The next size value at which to resize (capacity * load factor).
 *
 * @serial
 */
int threshold;
```

**modCount** 记录HashMap被改变的次数(在使用迭代器的时候，会判断此值是否跟期望的值一致，如果不一致，就会抛出异常)
```
/**
 * The number of times this HashMap has been structurally modified
 * Structural modifications are those that change the number of mappings in
 * the HashMap or otherwise modify its internal structure (e.g.,
 * rehash).  This field is used to make iterators on Collection-views of
 * the HashMap fail-fast.  (See ConcurrentModificationException).
 */
transient int modCount;
```

**loadFactor** HashMap的某一实例实际的负载因子,可以大于1
```
/**
 * The load factor for the hash table.
 *
 * @serial
 */
final float loadFactor;
```

### HashMap构造函数

1. 无参构造函数
```
/**
 * Constructs an empty <tt>HashMap</tt> with the default initial capacity
 * (16) and the default load factor (0.75).
 */
// 会将实例的负载因子赋值成默认的0.75
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}
```

2. 指定table初始大小的构造函数
```
/**
 * Constructs an empty <tt>HashMap</tt> with the specified initial
 * capacity and the default load factor (0.75).
 *
 * @param  initialCapacity the initial capacity.
 * @throws IllegalArgumentException if the initial capacity is negative.
 */
 // 会调用指定初始大小和初始负载因子的构造函数，只不过这里的负载因子给定的是默认的
public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}
```

3. 指定table初始大小&HashMap实例负载因子构造函数
```
/**
 * Constructs an empty <tt>HashMap</tt> with the specified initial
 * capacity and load factor.
 *
 * @param  initialCapacity the initial capacity
 * @param  loadFactor      the load factor
 * @throws IllegalArgumentException if the initial capacity is negative
 *         or the load factor is nonpositive
 */
public HashMap(int initialCapacity, float loadFactor) {
    // 会先判断指定大小是否在合法范围内，不合法则抛出异常
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    // 如果超出最大值，则默认选择最大值
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    // 判断负载因子是否在合法范围内，不合法则抛出异常    
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    // 构造初始resize的临界值，下文会介绍
    this.threshold = tableSizeFor(initialCapacity);
}
```

4. 给定集合初始化HashMap构造函数
```
/**
 * Constructs a new <tt>HashMap</tt> with the same mappings as the
 * specified <tt>Map</tt>.  The <tt>HashMap</tt> is created with
 * default load factor (0.75) and an initial capacity sufficient to
 * hold the mappings in the specified <tt>Map</tt>.
 *
 * @param   m the map whose mappings are to be placed in this map
 * @throws  NullPointerException if the specified map is null
 */
public HashMap(Map<? extends K, ? extends V> m) {
    // 设置负载因子为默认的负载因子0.75
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    // 操作集合
    putMapEntries(m, false);
}
```

### 源码分析

1. tableSizeFor(int initCapacity) 返回大于等于指定capacity的最小的2的幂
```
/**
 * Returns a power of two size for the given target capacity.
 */
static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
}
```

分析下，此处为什么要 cap - 1：

假设此处不减1，此时传入的cap刚好为2的幂，按照需求，此函数应该返回本身，但是实际却发挥cap * 2，下面介绍下这个过程。

如果cap = 0，则n = -1, 经过位移操作后,其依旧 < 0,那么此时返回1，没毛病
如果cap为1，则n = 0, 经过位移操作后,其依旧0，此时返回 n + 1,结果为1，没毛病
如果cap > 1, 则n > 0, 下面我们详细分析这种情况：
第一次右移
```
n |= n >>> 1
```
由于n不能与0,那么n的二进制总有一位为1，此时考虑最高位1，在经过无符号右移1位后，再与本身取或操作，靠近最高位的1的右边紧邻的一位也位1，类似下边
```
n = 00001XXXXX(共32位)
n >>> 1 => 0000001XXXX
00001XXXXX | 0000001XXXX ==> 000011XXXX
```

第二次右移
```
n |= n >>> 2

000011XXXX >>> 2 ==> 00000011XX
000011XXXX | 00000011XX ==> 00001111XX

```

第三次右移
```
n |= n >>> 4;
00001111XX >>> 4 ==> 0000000011
00001111XX | 0000000011 ==> 0000111111
```
依此类推，n |= n >>> 32,最后会出现一个0{X}1111{Y}，X + Y = 32,X代表右边有X位0， Y代表左边有Y位1，最大位32位1,当出现32位1的时候，会取MAXIMUM_CAPACITY,如果还没有达到32个1，那么n取 n + 1，刚好是大于等于cap 的最小的2的幂数

此时回看上方构造函数第4个，可能会有个疑问
```
this.threshold = tableSizeFor(initialCapacity);
```
为什么会直接赋值给threshold呢？而不是
```
this.threshold = tableSizeFor(initialCapcity) * this.loadFactor;
```
这样才是符合threshold的定义啊（当HashMap的size达到threshold的时候，HashMap会进行扩容）

**解释**
我们注意到，在构造函数里，并没有对table进行初始化工作，针对初始化的时候，是在put操作里进行的，所以这里直接对threshold进行赋值。
[此处参考](https://blog.csdn.net/fan2012huan/article/details/51097331)

2. put(K k, V v)，执行put操作
```
/**
 * Associates the specified value with the specified key in this map.
 * If the map previously contained a mapping for the key, the old
 * value is replaced.
 *
 * @param key key with which the specified value is to be associated
 * @param value value to be associated with the specified key
 * @return the previous value associated with <tt>key</tt>, or
 *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
 *         (A <tt>null</tt> return can also indicate that the map
 *         previously associated <tt>null</tt> with <tt>key</tt>.)
 */
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
```
观察源码发现，其实其调用的是putVal(hash(key), key, value, false, true)，关于hash(key)的分析，我们看下**int hash(Object key)**

3. int hash(Object key)，计算key的hash值
```
/**
 * Computes key.hashCode() and spreads (XORs) higher bits of hash
 * to lower.  Because the table uses power-of-two masking, sets of
 * hashes that vary only in bits above the current mask will
 * always collide. (Among known examples are sets of Float keys
 * holding consecutive whole numbers in small tables.)  So we
 * apply a transform that spreads the impact of higher bits
 * downward. There is a tradeoff between speed, utility, and
 * quality of bit-spreading. Because many common sets of hashes
 * are already reasonably distributed (so don't benefit from
 * spreading), and because we use trees to handle large sets of
 * collisions in bins, we just XOR some shifted bits in the
 * cheapest possible way to reduce systematic lossage, as well as
 * to incorporate impact of the highest bits that would otherwise
 * never be used in index calculations because of table bounds.
 */
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
从上面的代码可以看到，key的hash值高16位不变，低16位与高16位取异或操作（h >>> 16代表无符号右移16位，即高16位全部为0，原先的高16位变成右移后的低16位），如下：
```
h = key.hashCode(); ===> 1010 0011 1100 0011 - 0011 1100 0011 1100
n = h >>> 16;       ===> 0000 0000 0000 0000 - 1010 0011 1100 0011

h = h ^ n;          ===> 1010 0011 1100 0011 - 1001 1111 1111 1111
```
**为什么要这么操作呢**

看下文我们知道，HashMap的下标计算方法如下:
```
n = table.length;
index = (n - 1) & hash;
```
我们在上面介绍到，由于HashMap的table大小都是2的幂等数，所以其下标计算方法仅仅与其**低位**有关，借用上边的例子，我们做个简单分析
```
h = key.hashCode(); ===> 1010 0011 1100 0011 - 0011 1100 0011 1100
n = h >>> 16;       ===> 0000 0000 0000 0000 - 1010 0011 1100 0011

h = h ^ n;          ===> 1010 0011 1100 0011 - 1001 1111 1111 1111

假设table的大小为默认的16
n = table.length - 1; ===> 0000 0000 0000 0000 - 0000 0000 0000 1111

h;                    ===> 1010 0011 1100 0011 - 1001 1111 1111 1111

index = n & h;        ===> 0000 0000 0000 0000 - 0000 0000 0000 1111  ==> 1111 = 15
```
由上边的演算可以看出，确实只是地位参与了计算，如果这样的话，就很容易产生index碰撞。设计者权衡了速度，实用性和质量等方面，将key的hash值的高16位和低16位做异或操作来减少影响。
设计者考虑到现在的hashCode的分布已经不错了，而且当发生较大碰撞时，会采用树进行存储来较少链表长度。仅仅异或一下，不仅对系统开销小，而且让key的hashCode的高位 & 低位都参与了计算，
从而减少碰撞的概率。[此处参考](https://blog.csdn.net/fan2012huan/article/details/51097331)

接着我们来看putVal()操作

4. final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) 执行put操作
```
/**
 * Implements Map.put and related methods
 *
 * @param hash hash for key
 * @param key the key
 * @param value the value to put
 * @param onlyIfAbsent if true, don't change existing value
 * @param evict if false, the table is in creation mode.
 * @return previous value, or null if none
 */
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    // 首先定义一个基础变量
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 判断是否为第一次put操作，第一次put 操作，table为null, 或者table.length == 0， 此时需要进行resize()操作，在下边的5.resize()方法介绍里可以看的具体的操作
    if ((tab = table) == null || (n = tab.length) == 0)
        // 经过下面的分析，此处会返回一个分配好空间的空数组，用tab接收，n为数组的大小
        n = (tab = resize()).length;
    // i = (n - 1) & hash，此处为获取key所在的index，在上面已经介绍过，这样做可以充分考虑利用key hashCode的高、低16位
    // p = tab[i], 返回key所在的node, 此处判断p == null,
    if ((p = tab[i = (n - 1) & hash]) == null)
        // 说明数组的该位置内容未存放任何值，则将key, value赋值给新产生的newNode里，并且存放到i对应的数组位置上， newNode()比较简单，仅实例化一个node,此处不做赘述
        tab[i] = newNode(hash, key, value, null);
    else {
        // 如果找到的p不为空，则说明key存在hash碰撞
        Node<K,V> e; K k;
        // 如果key相同，那么直接用p替换e，此时说明，p节点，就在数组上的位置，即在"链表"的头上
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 如果不在第一个位置，则需要判断这个节点是否已经散列成了一个树节点，如果是树节点，则进行想要的树操作（红黑树不在本文讨论范围中）    
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 如果不在第一个位置，且节点不是树节点，那么需要遍历该节点所在的链表，然后找到相应的操作（是插入还是覆盖），for循环开始遍历链表
            for (int binCount = 0; ; ++binCount) {
                // 一直往下找，如果没找到key相同的，则在链表的尾部加入该节点
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // 在链表的尾部加入该节点后，判断链表长度是否达到散列成红黑树的临界值，如果达到了，则需要将该链变成红黑树
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    // 插入新的内容后，跳出循环
                    break;
                }
                // 如果找到了相同的key，则直接跳出循环，否则接着往下找
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // 如果e不为空，则说明key重复， 会根据onlyIfAbsent的值决定是否替换value, 返回永远是oldValue, 因为key存在，所有不会执行下面的方法
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    // 修改次数+1
    ++modCount;
    // 判断HashMap键值对的个数在插入新的节点后是否 > 临界值，如果是，则需要进行扩容操作，也即执行resize()，执行到这里，说明是put一个不存在的节点，所以返回的是null，但是不能根据是否返回null，判断是否存在重复key,因为HashMap允许value为null
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

5. final Node<K,V>[] resize(); HashMap初始化或两倍扩容操作
```
/**
 * Initializes or doubles table size.  If null, allocates in
 * accord with initial capacity target held in field threshold.
 * Otherwise, because we are using power-of-two expansion, the
 * elements from each bin must either stay at same index, or move
 * with a power of two offset in the new table.
 *
 * @return the table
 */
final Node<K,V>[] resize() {
    // 将数组引用赋值给新定义的变量
    Node<K,V>[] oldTab = table;
    // 根据table是否为初始化操作决定原先的数组大小
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    // 如果原先的数组不为空，则是需要进行扩容操作
    if (oldCap > 0) {
        // 如果老数组的容量已经达到最大容量，则直接返回旧的数组
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 对数组进行两倍扩容
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    // 数组为空，但是指定了initialCapacity, 对应构造函数里的第二类或者第三类，这里也就说明了为什么tableSizeFor会直接赋值给threshold，这里会将threhold赋值给capacity
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    // 对应无参构造函数，直接new HashMap()的情况
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    // 对应第二或第三类构造函数
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    // 将新的临界值赋值给threshold
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    // 按照新的capacity重新开辟数组空间，并且赋值给table, 判断原来是否数组不为空，如果为空，则直接返回，如果不为空，则进行复制操作
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        // 以下内容为将数组复制到新的数组中， 上面介绍到，扩容，会将数组大小增加为原来的2倍
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            // 用e来接收数组中不为空的node,即每条链的第一个节点，只有当e不为空时，才代表此处节点有内容，才有进行复制的必要
            if ((e = oldTab[j]) != null) {
                // 将旧的table的第一位置释放，方便垃圾回收
                oldTab[j] = null;
                // 如果仅存在第一个节点，那么将key在新的table中计算得到的位置，赋值上即可，前边提到过index = hash & (table.length - 1)
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                // 如果是一个树节点，则按照树的操作进行，树的相关操作，将在后边进行解析讲解
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    // 即说明该条链存在且不是树
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    // 由于每次扩容都是扩展到原来数组大小到2倍，所以会是这种情况，原index要么还是在原来到位置，要么会出现在原index + oldCap的位置,此处比较精巧，免去key需要重新进行rehash计算的过程，HashMap的线程不安全会提现在这个地方
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```
在查阅资料的时候，发现了一篇好文章，写的非常详细，[原文链接](https://blog.csdn.net/login_sonata/article/details/76598675)

6. V get(K k),通过k，获取元素的值
```
/**
 * Returns the value to which the specified key is mapped,
 * or {@code null} if this map contains no mapping for the key.
 *
 * <p>More formally, if this map contains a mapping from a key
 * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
 * key.equals(k))}, then this method returns {@code v}; otherwise
 * it returns {@code null}.  (There can be at most one such mapping.)
 *
 * <p>A return value of {@code null} does not <i>necessarily</i>
 * indicate that the map contains no mapping for the key; it's also
 * possible that the map explicitly maps the key to {@code null}.
 * The {@link #containsKey containsKey} operation may be used to
 * distinguish these two cases.
 *
 * @see #put(Object, Object)
 */
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

/**
 * Implements Map.get and related methods
 *
 * @param hash hash for key
 * @param key the key
 * @return the node, or null if none
 */
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}
```
阅读源码发现，通过计算key的hash值，获取在数组中的位置，index = hash & (table.length - 1)

7. containsKey(K k) 通过key的值，判断其是否存在key
```
/**
 * Returns <tt>true</tt> if this map contains a mapping for the
 * specified key.
 *
 * @param   key   The key whose presence in this map is to be tested
 * @return <tt>true</tt> if this map contains a mapping for the specified
 * key.
 */
public boolean containsKey(Object key) {
    // 通过key获取node，判断是否为空
    return getNode(hash(key), key) != null;
}
```

8. containsValue(Object v)，判断是否存在指定的值
```
/**
 * Returns <tt>true</tt> if this map maps one or more keys to the
 * specified value.
 *
 * @param value value whose presence in this map is to be tested
 * @return <tt>true</tt> if this map maps one or more keys to the
 *         specified value
 */
public boolean containsValue(Object value) {
    Node<K,V>[] tab; V v;
    if ((tab = table) != null && size > 0) {
        for (int i = 0; i < tab.length; ++i) {
            for (Node<K,V> e = tab[i]; e != null; e = e.next) {
                if ((v = e.value) == value ||
                    (value != null && value.equals(v)))
                    return true;
            }
        }
    }
    return false;
}
```
通过遍历，判断是否存在某个值

9. remove(K k)，通过key，移除某个对象
```
/**
 * Removes the mapping for the specified key from this map if present.
 *
 * @param  key key whose mapping is to be removed from the map
 * @return the previous value associated with <tt>key</tt>, or
 *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
 *         (A <tt>null</tt> return can also indicate that the map
 *         previously associated <tt>null</tt> with <tt>key</tt>.)
 */
public V remove(Object key) {
    Node<K,V> e;
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}

/**
 * Implements Map.remove and related methods
 *
 * @param hash hash for key
 * @param key the key
 * @param value the value to match if matchValue, else ignored
 * @param matchValue if true only remove if value is equal
 * @param movable if false do not move other nodes while removing
 * @return the node, or null if none
 */
final Node<K,V> removeNode(int hash, Object key, Object value,
                           boolean matchValue, boolean movable) {
    Node<K,V>[] tab; Node<K,V> p; int n, index;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (p = tab[index = (n - 1) & hash]) != null) {
        Node<K,V> node = null, e; K k; V v;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
        else if ((e = p.next) != null) {
            if (p instanceof TreeNode)
                node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
            else {
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                         (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
        }
        if (node != null && (!matchValue || (v = node.value) == value ||
                             (value != null && value.equals(v)))) {
            if (node instanceof TreeNode)
                ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
            else if (node == p)
                tab[index] = node.next;
            else
                p.next = node.next;
            ++modCount;
            --size;
            afterNodeRemoval(node);
            return node;
        }
    }
    return null;
}
```
实则是对链表 或者 树的移除操作

以上内容即为HashMap源码分析的部分内容，主要涉及到的是HashMap的一些常规操作，包括每个参数的意义，初始化，put，扩容等等，一定要弄清楚做put操作，HashMap会经历什么。其中，在源码阅读过程中，发现HashMap确实有设计非常优美的地方，比如扩容的算法，key数组位置的计算等。
里边设计红黑树以及线程不安全的内容，将在后期的文档中介绍。