package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.redis.connection.MessageListener;
import cn.choleece.base.framework.redis.connection.RedisInvalidSubscriptionException;
import cn.choleece.base.framework.redis.connection.Subscription;
import cn.choleece.base.framework.redis.connection.util.ByteArrayWrapper;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:00
 */
public abstract class AbstractSubscription implements Subscription {

    /**
     * 通道
     */
    private final Collection<ByteArrayWrapper> channels;

    /**
     * 模式
     */
    private final Collection<ByteArrayWrapper> patterns;

    private final AtomicBoolean alive;

    /**
     * 消息监听器
     */
    private final MessageListener listener;

    protected AbstractSubscription(MessageListener listener, @Nullable byte[][] channels, @Nullable byte[][] patterns) {
        this.channels = new ArrayList(2);
        this.patterns = new ArrayList(2);
        this.alive = new AtomicBoolean(true);
        Assert.notNull(listener, "MessageListener must not be null!");
        this.listener = listener;
        synchronized(this.channels) {
            add(this.channels, channels);
        }

        synchronized(this.patterns) {
            add(this.patterns, patterns);
        }
    }

    protected abstract void doSubscribe(byte[]... var1);

    protected abstract void doUnsubscribe(boolean var1, byte[]... var2);

    protected abstract void doPsubscribe(byte[]... var1);

    protected abstract void doPUnsubscribe(boolean var1, byte[]... var2);

    @Override
    public void close() {
        this.doClose();
    }

    protected abstract void doClose();

    @Override
    public MessageListener getListener() {
        return this.listener;
    }

    @Override
    public Collection<byte[]> getChannels() {
        synchronized(this.channels) {
            return clone(this.channels);
        }
    }

    @Override
    public Collection<byte[]> getPatterns() {
        synchronized(this.patterns) {
            return clone(this.patterns);
        }
    }

    @Override
    public void pSubscribe(byte[]... patterns) {
        this.checkPulse();
        Assert.notEmpty(patterns, "at least one pattern required");
        synchronized(this.patterns) {
            add(this.patterns, patterns);
        }

        this.doPsubscribe(patterns);
    }

    @Override
    public void pUnsubscribe() {
        this.pUnsubscribe((byte[][])((byte[][])null));
    }

    @Override
    public void subscribe(byte[]... channels) {
        this.checkPulse();
        Assert.notEmpty(channels, "at least one channel required");
        synchronized(this.channels) {
            add(this.channels, channels);
        }

        this.doSubscribe(channels);
    }

    @Override
    public void unsubscribe() {
        this.unsubscribe((byte[][])((byte[][])null));
    }

    @Override
    public void pUnsubscribe(@Nullable byte[]... patts) {
        if (this.isAlive()) {
            if (ObjectUtils.isEmpty(patts)) {
                if (this.patterns.isEmpty()) {
                    return;
                }

                synchronized(this.patterns) {
                    patts = (byte[][])this.getPatterns().toArray(new byte[this.patterns.size()][]);
                    this.doPUnsubscribe(true, patts);
                    this.patterns.clear();
                }
            } else {
                this.doPUnsubscribe(false, patts);
                synchronized(this.patterns) {
                    remove(this.patterns, patts);
                }
            }

            this.closeIfUnsubscribed();
        }
    }

    @Override
    public void unsubscribe(@Nullable byte[]... chans) {
        if (this.isAlive()) {
            if (ObjectUtils.isEmpty(chans)) {
                if (this.channels.isEmpty()) {
                    return;
                }

                synchronized(this.channels) {
                    chans = (byte[][])this.getChannels().toArray(new byte[this.channels.size()][]);
                    this.doUnsubscribe(true, chans);
                    this.channels.clear();
                }
            } else {
                this.doUnsubscribe(false, chans);
                synchronized(this.channels) {
                    remove(this.channels, chans);
                }
            }

            this.closeIfUnsubscribed();
        }
    }

    @Override
    public boolean isAlive() {
        return this.alive.get();
    }

    private void checkPulse() {
        if (!this.isAlive()) {
            throw new RedisInvalidSubscriptionException("Subscription has been unsubscribed and cannot be used anymore");
        }
    }

    private void closeIfUnsubscribed() {
        if (this.channels.isEmpty() && this.patterns.isEmpty()) {
            this.alive.set(false);
            this.doClose();
        }

    }

    private static Collection<byte[]> clone(Collection<ByteArrayWrapper> col) {
        Collection<byte[]> list = new ArrayList(col.size());
        Iterator var2 = col.iterator();

        while(var2.hasNext()) {
            ByteArrayWrapper wrapper = (ByteArrayWrapper)var2.next();
            list.add(wrapper.getArray().clone());
        }

        return list;
    }

    private static void add(Collection<ByteArrayWrapper> col, @Nullable byte[]... bytes) {
        if (!ObjectUtils.isEmpty(bytes)) {
            byte[][] var2 = bytes;
            int var3 = bytes.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte[] bs = var2[var4];
                col.add(new ByteArrayWrapper(bs));
            }
        }

    }

    private static void remove(Collection<ByteArrayWrapper> col, @Nullable byte[]... bytes) {
        if (!ObjectUtils.isEmpty(bytes)) {
            byte[][] var2 = bytes;
            int var3 = bytes.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte[] bs = var2[var4];
                col.remove(new ByteArrayWrapper(bs));
            }
        }
    }
}
