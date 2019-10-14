package cn.choleece.base.framework.redis.core;

import java.io.Closeable;
import java.util.Iterator;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:38
 **/
public interface Cursor<T> extends Iterator<T>, Closeable {

    long getCursorId();

    boolean isClosed();

    Cursor<T> open();

    long getPosition();
}