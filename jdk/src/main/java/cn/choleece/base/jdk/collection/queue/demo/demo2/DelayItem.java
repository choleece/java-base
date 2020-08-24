package cn.choleece.base.jdk.collection.queue.demo.demo2;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayItem<K> implements Delayed {

    private K k;

    private long liveTime;

    private long removeTime;

    public DelayItem(K t,long liveTime){
        this.setK(t);
        this.liveTime = liveTime;
        this.removeTime = TimeUnit.NANOSECONDS.convert(liveTime, TimeUnit.NANOSECONDS) + System.nanoTime();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(removeTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o == null) {
            return 1;
        }
        if (o == this) {
            return  0;
        }
        if (o instanceof DelayItem){
            DelayItem<K> tmpDelayedItem = (DelayItem<K>) o;
            if (liveTime > tmpDelayedItem.liveTime ) {
                return 1;
            } else if (liveTime == tmpDelayedItem.liveTime) {
                return 0;
            } else {
                return -1;
            }
        }
        long diff = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return diff > 0 ? 1:diff == 0? 0:-1;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DelayItem) {
            return o.hashCode() == k.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return k.hashCode();
    }
}
