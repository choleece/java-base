package cn.choleece.base.framework.redis.connection;

import java.util.Arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:48
 **/
public class DefaultTuple implements RedisZSetCommands.Tuple {
    private final Double score;
    private final byte[] value;

    public DefaultTuple(byte[] value, Double score) {
        this.score = score;
        this.value = value;
    }

    @Override
    public Double getScore() {
        return this.score;
    }

    @Override
    public byte[] getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (!(obj instanceof DefaultTuple)) {
            return false;
        } else {
            DefaultTuple other = (DefaultTuple)obj;
            if (this.score == null) {
                if (other.score != null) {
                    return false;
                }
            } else if (!this.score.equals(other.score)) {
                return false;
            }

            return Arrays.equals(this.value, other.value);
        }
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.score == null ? 0 : this.score.hashCode());
        result = 31 * result + Arrays.hashCode(this.value);
        return result;
    }

    @Override
    public int compareTo(Double o) {
        Double d = this.score == null ? 0.0D : this.score;
        Double a = o == null ? 0.0D : o;
        return d.compareTo(a);
    }
}

