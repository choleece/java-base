package cn.choleece.base.framework.redis.core;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 15:50
 */
public class ScanIteration<T> implements Iterable<T> {

    private final long cursorId;
    private final Collection<T> items;

    /**
     * @param cursorId
     * @param items
     */
    public ScanIteration(long cursorId, @Nullable Collection<T> items) {

        this.cursorId = cursorId;
        this.items = (items != null ? new ArrayList<>(items) : Collections.emptyList());
    }

    /**
     * The cursor id to be used for subsequent requests.
     *
     * @return
     */
    public long getCursorId() {
        return cursorId;
    }

    /**
     * Get the items returned.
     *
     * @return
     */
    public Collection<T> getItems() {
        return items;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

}

