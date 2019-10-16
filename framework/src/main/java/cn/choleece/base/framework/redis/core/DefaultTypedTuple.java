package cn.choleece.base.framework.redis.core;

import org.springframework.lang.Nullable;

import java.util.Arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:47
 **/
public class DefaultTypedTuple<V> implements ZSetOperations.TypedTuple<V> {
    @Nullable
    private final Double score;
    @Nullable
    private final V value;

    public DefaultTypedTuple(@Nullable V value, @Nullable Double score) {
        this.score = score;
        this.value = value;
    }

    @Nullable
    @Override
    public Double getScore() {
        return this.score;
    }

    @Nullable
    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.score == null ? 0 : this.score.hashCode());
        result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof DefaultTypedTuple)) {
            return false;
        } else {
            DefaultTypedTuple<?> other = (DefaultTypedTuple)obj;
            if (this.score == null) {
                if (other.score != null) {
                    return false;
                }
            } else if (!this.score.equals(other.score)) {
                return false;
            }

            if (this.value == null) {
                if (other.value != null) {
                    return false;
                }
            } else {
                if (this.value instanceof byte[]) {
                    if (!(other.value instanceof byte[])) {
                        return false;
                    }

                    return Arrays.equals((byte[])((byte[])this.value), (byte[])((byte[])other.value));
                }

                if (!this.value.equals(other.value)) {
                    return false;
                }
            }

            return true;
        }
    }

    public int compareTo(Double o) {
        double thisScore = this.score == null ? 0.0D : this.score;
        double otherScore = o == null ? 0.0D : o;
        return Double.compare(thisScore, otherScore);
    }

    @Override
    public int compareTo(ZSetOperations.TypedTuple<V> o) {
        return o == null ? this.compareTo(0.0D) : this.compareTo(o.getScore());
    }
}
