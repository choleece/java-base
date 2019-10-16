package cn.choleece.base.framework.redis.core;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 11:23
 */
public class ConvertingCursor<S, T> implements Cursor<T> {
    private Cursor<S> delegate;
    private Converter<S, T> converter;

    public ConvertingCursor(Cursor<S> cursor, Converter<S, T> converter) {
        Assert.notNull(cursor, "Cursor delegate must not be 'null'.");
        Assert.notNull(cursor, "Converter must not be 'null'.");
        this.delegate = cursor;
        this.converter = converter;
    }

    @Override
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override
    @Nullable
    public T next() {
        return this.converter.convert(this.delegate.next());
    }

    @Override
    public void remove() {
        this.delegate.remove();
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override
    public long getCursorId() {
        return this.delegate.getCursorId();
    }

    @Override
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override
    public Cursor<T> open() {
        this.delegate = this.delegate.open();
        return this;
    }

    @Override
    public long getPosition() {
        return this.delegate.getPosition();
    }
}
