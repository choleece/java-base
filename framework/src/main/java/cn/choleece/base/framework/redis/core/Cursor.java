package cn.choleece.base.framework.redis.core;

import java.io.Closeable;
import java.util.Iterator;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:38
 **/
public interface Cursor<T> extends Iterator<T>, Closeable {

    /**
     * Get the reference cursor. <br>
     * <strong>NOTE:</strong> the id might change while iterating items.
     *
     * @return
     */
    long getCursorId();

    /**
     * @return Returns true if cursor closed.
     */
    boolean isClosed();

    /**
     * Opens cursor and returns itself.
     *
     * @return
     */
    Cursor<T> open();

    /**
     * @return Returns the current position of the cursor.
     */
    long getPosition();

}