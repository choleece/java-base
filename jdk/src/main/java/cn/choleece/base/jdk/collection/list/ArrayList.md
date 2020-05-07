## ArrayList

```
// 这三个接口为标记接口， 还有一个(java.rmi.Remote)，没有具体的属性和方法，是一种设计思想，Java中的一个标记接口表示的的是一种类的特性，实现了该标记接口的类则具有该特性
public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable

// 可随机存取，可对对象内包含对元素进行随机存取，时间复杂度为O(1)
public interface RandomAccess {
}

// 可被克隆，该接口标记一个类的对象是否有安全的clone方法， 这里有个深克隆&浅克隆的知识
public interface Cloneable {
}

// 可序列化, 实现了这个接口，表示可以进行序列化和反序列化
public interface Serializable {
}

public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E>
public abstract class AbstractCollection<E> implements Collection<E>
public interface List<E> extends Collection<E>
public interface Collection<E> extends Iterable<E>

public interface Iterable<T> {
    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    Iterator<T> iterator();

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @implSpec
     * <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @since 1.8
     */
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @implSpec
     * The default implementation creates an
     * <em><a href="Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     *
     * @implNote
     * The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @since 1.8
     */
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```