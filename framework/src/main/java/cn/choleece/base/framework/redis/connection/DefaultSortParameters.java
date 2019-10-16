package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:56
 **/
public class DefaultSortParameters implements SortParameters {
    @Nullable
    private byte[] byPattern;
    @Nullable
    private Range limit;
    private final List<byte[]> getPattern;
    @Nullable
    private Order order;
    @Nullable
    private Boolean alphabetic;

    public DefaultSortParameters() {
        this((byte[])null, (Range)null, (byte[][])null, (Order)null, (Boolean)null);
    }

    public DefaultSortParameters(@Nullable Range limit, @Nullable Order order, @Nullable Boolean alphabetic) {
        this((byte[])null, limit, (byte[][])null, order, alphabetic);
    }

    public DefaultSortParameters(@Nullable byte[] byPattern, @Nullable Range limit, @Nullable byte[][] getPattern, @Nullable Order order, @Nullable Boolean alphabetic) {
        this.getPattern = new ArrayList(4);
        this.byPattern = byPattern;
        this.limit = limit;
        this.order = order;
        this.alphabetic = alphabetic;
        this.setGetPattern(getPattern);
    }

    @Override
    @Nullable
    public byte[] getByPattern() {
        return this.byPattern;
    }

    public void setByPattern(byte[] byPattern) {
        this.byPattern = byPattern;
    }

    @Override
    public Range getLimit() {
        return this.limit;
    }

    public void setLimit(Range limit) {
        this.limit = limit;
    }

    @Override
    public byte[][] getGetPattern() {
        return (byte[][])this.getPattern.toArray(new byte[this.getPattern.size()][]);
    }

    @Nullable
    public void addGetPattern(byte[] gPattern) {
        this.getPattern.add(gPattern);
    }

    public void setGetPattern(@Nullable byte[][] gPattern) {
        this.getPattern.clear();
        if (gPattern != null) {
            Collections.addAll(this.getPattern, gPattern);
        }
    }

    @Override
    @Nullable
    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    @Nullable
    public Boolean isAlphabetic() {
        return this.alphabetic;
    }

    public void setAlphabetic(Boolean alphabetic) {
        this.alphabetic = alphabetic;
    }

    public DefaultSortParameters order(Order order) {
        this.setOrder(order);
        return this;
    }

    public DefaultSortParameters alpha() {
        this.setAlphabetic(true);
        return this;
    }

    public DefaultSortParameters asc() {
        this.setOrder(Order.ASC);
        return this;
    }

    public DefaultSortParameters desc() {
        this.setOrder(Order.DESC);
        return this;
    }

    public DefaultSortParameters numeric() {
        this.setAlphabetic(false);
        return this;
    }

    public DefaultSortParameters get(byte[] pattern) {
        this.addGetPattern(pattern);
        return this;
    }

    public DefaultSortParameters by(byte[] pattern) {
        this.setByPattern(pattern);
        return this;
    }

    public DefaultSortParameters limit(long start, long count) {
        this.setLimit(new Range(start, count));
        return this;
    }
}