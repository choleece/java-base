package cn.choleece.base.jdk.collection.queue.demo.demo2;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;

/**
 * 延迟队列应用场景二
 * 向缓存添加内容时，给每一个key设定过期时间，系统自动将超过过期时间的key清除。
 * 当向缓存中添加key-value对时，如果这个key在缓存中存在并且还没有过期，需要用这个key对应的新过期时间
 * 为了能够让DelayQueue将其已保存的key删除，需要重写实现Delayed接口添加到DelayQueue的DelayedItem的hashCode函数和equals函数
 * 当缓存关闭，监控程序也应关闭，因而监控线程应当用守护线程
 * @param <K>
 * @param <V>
 */
public class Cache<K, V> {

    public ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
    public DelayQueue<DelayItem<K>> queue = new DelayQueue<DelayItem<K>>();

    public void put(K k, V v, long liveTime) {
        V v1 = map.put(k, v);

        DelayItem<K> delayItem = new DelayItem<>(k, liveTime);

        if (v1 != null) {
            queue.remove(delayItem);
        }
        queue.put(delayItem);
    }

    public Cache() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                deamonCheckOverdueKey();
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public void deamonCheckOverdueKey() {
        while (true) {
            System.out.println("我来扫描呀。。。。");
            DelayItem delayItem = queue.poll();
            if (delayItem != null) {
                map.remove(delayItem.getK());
                System.out.println(delayItem.getK() + "已到期");
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int cacheNum = 10;

        int liveTime = 0;

        Cache<String, Integer> cache = new Cache<>();

        for (int i = 0; i < cacheNum; i++) {
            liveTime = random.nextInt(3000);
            System.out.println(i + " live " + liveTime);
            cache.put(i + "", i, random.nextInt(liveTime));
            if (random.nextInt(cacheNum) > 7) {
                liveTime = random.nextInt(3000);
                System.out.println(i+"  "+liveTime);
                cache.put(i+"", i, random.nextInt(liveTime));
            }
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("结束");
    }

}
