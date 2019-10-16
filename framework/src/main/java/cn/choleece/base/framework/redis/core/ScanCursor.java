package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.exception.InvalidDataAccessApiUsageException;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 15:48
 */
public abstract class ScanCursor<T> implements Cursor<T> {

    private @Nullable
    CursorState state;
    private long cursorId;
    private @Nullable
    Iterator<T> delegate;
    private @Nullable final ScanOptions scanOptions;
    private long position;

    /**
     * Crates new {@link ScanCursor} with {@code id=0} and {@link ScanOptions#NONE}
     */
    public ScanCursor() {
        this(ScanOptions.NONE);
    }

    /**
     * Crates new {@link ScanCursor} with {@code id=0}.
     *
     * @param options
     */
    public ScanCursor(ScanOptions options) {
        this(0, options);
    }

    /**
     * Crates new {@link ScanCursor} with {@link ScanOptions#NONE}
     *
     * @param cursorId
     */
    public ScanCursor(long cursorId) {
        this(cursorId, ScanOptions.NONE);
    }

    /**
     * Crates new {@link ScanCursor}
     *
     * @param cursorId
     * @param options Defaulted to {@link ScanOptions#NONE} if nulled.
     */
    public ScanCursor(long cursorId, ScanOptions options) {

        this.scanOptions = options != null ? options : ScanOptions.NONE;
        this.cursorId = cursorId;
        this.state = CursorState.READY;
        this.delegate = Collections.<T> emptyList().iterator();
    }

    private void scan(long cursorId) {

        ScanIteration<T> result = doScan(cursorId, this.scanOptions);
        processScanResult(result);
    }

    /**
     * Performs the actual scan command using the native client implementation. The given {@literal options} are never
     * {@code null}.
     *
     * @param cursorId
     * @param options
     * @return
     */
    protected abstract ScanIteration<T> doScan(long cursorId, ScanOptions options);

    /**
     * Initialize the {@link Cursor} prior to usage.
     */
    public final ScanCursor<T> open() {

        if (!isReady()) {
            throw new InvalidDataAccessApiUsageException("Cursor already " + state + ". Cannot (re)open it.");
        }

        state = CursorState.OPEN;
        doOpen(cursorId);

        return this;
    }

    /**
     * Customization hook when calling {@link #open()}.
     *
     * @param cursorId
     */
    protected void doOpen(long cursorId) {
        scan(cursorId);
    }

    private void processScanResult(ScanIteration<T> result) {

        if (result == null) {

            resetDelegate();
            state = CursorState.FINISHED;
            return;
        }

        cursorId = Long.valueOf(result.getCursorId());

        if (isFinished(cursorId)) {
            state = CursorState.FINISHED;
        }

        if (!CollectionUtils.isEmpty(result.getItems())) {
            delegate = result.iterator();
        } else {
            resetDelegate();
        }
    }

    /**
     * Check whether {@code cursorId} is finished.
     *
     * @param cursorId the cursor Id
     * @return {@literal true} if the cursor is considered finished, {@literal false} otherwise.s
     * @since 2.1
     */
    protected boolean isFinished(long cursorId) {
        return cursorId == 0;
    }

    private void resetDelegate() {
        delegate = Collections.<T> emptyList().iterator();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.core.Cursor#getCursorId()
     */
    @Override
    public long getCursorId() {
        return cursorId;
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {

        assertCursorIsOpen();

        while (!delegate.hasNext() && !CursorState.FINISHED.equals(state)) {
            scan(cursorId);
        }

        if (delegate.hasNext()) {
            return true;
        }

        if (cursorId > 0) {
            return true;
        }

        return false;
    }

    private void assertCursorIsOpen() {

        if (isReady() || isClosed()) {
            throw new InvalidDataAccessApiUsageException("Cannot access closed cursor. Did you forget to call open()?");
        }
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public T next() {

        assertCursorIsOpen();

        if (!hasNext()) {
            throw new NoSuchElementException("No more elements available for cursor " + cursorId + ".");
        }

        T next = moveNext(delegate);
        position++;

        return next;
    }

    /**
     * Fetch the next item from the underlying {@link Iterable}.
     *
     * @param source
     * @return
     */
    protected T moveNext(Iterator<T> source) {
        return source.next();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported");
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public final void close() throws IOException {

        try {
            doClose();
        } finally {
            state = CursorState.CLOSED;
        }
    }

    /**
     * Customization hook for cleaning up resources on when calling {@link #close()}.
     */
    protected void doClose() {}

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.core.Cursor#isClosed()
     */
    @Override
    public boolean isClosed() {
        return state == CursorState.CLOSED;
    }

    protected final boolean isReady() {
        return state == CursorState.READY;
    }

    protected final boolean isOpen() {
        return state == CursorState.OPEN;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.core.Cursor#getPosition()
     */
    @Override
    public long getPosition() {
        return position;
    }

    /**
     * @author Thomas Darimont
     */
    enum CursorState {
        READY, OPEN, FINISHED, CLOSED;
    }
}