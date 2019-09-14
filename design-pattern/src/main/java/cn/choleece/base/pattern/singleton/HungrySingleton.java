package cn.choleece.base.pattern.singleton;

/**
 * 饿汉式单例，一开始就创建一个，不论用还是不用
 */
public class HungrySingleton {

    public static final HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}
