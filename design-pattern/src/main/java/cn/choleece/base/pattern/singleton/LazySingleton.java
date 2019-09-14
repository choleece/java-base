package cn.choleece.base.pattern.singleton;

/**
 * 懒汉方式实现 只有在使用的时候，才会对其进行实例化
 * @author choleece
 */
public class LazySingleton {

    /**
     * 保证instance对所有线程可见
     */
    private static volatile LazySingleton instance;

    /**
     * 设置成私有的构造方法，为了让其不能被外部使用
     */
    private LazySingleton() {
    }

    public static synchronized LazySingleton getInstance() {

        /**
         * 如果实例为空，则创建一个,只有在使用对时候才对其进行创建，这样有个问题就是存在竞争，每次getInstance时都会有锁竞争，会影响性能
         */
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
