package cn.choleece.base.framework.redis.connection;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 11:19
 */
public class DefaultStringTuple extends DefaultTuple implements StringRedisConnection.StringTuple {
    private final String valueAsString;

    public DefaultStringTuple(byte[] value, String valueAsString, Double score) {
        super(value, score);
        this.valueAsString = valueAsString;
    }

    public DefaultStringTuple(RedisZSetCommands.Tuple tuple, String valueAsString) {
        super(tuple.getValue(), tuple.getScore());
        this.valueAsString = valueAsString;
    }

    public String getValueAsString() {
        return this.valueAsString;
    }

    @Override
    public String toString() {
        return "DefaultStringTuple[value=" + this.getValueAsString() + ", score=" + this.getScore() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof DefaultStringTuple)) {
            return false;
        } else {
            DefaultStringTuple other = (DefaultStringTuple)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                Object this$valueAsString = this.getValueAsString();
                Object other$valueAsString = other.getValueAsString();
                if (this$valueAsString == null) {
                    if (other$valueAsString != null) {
                        return false;
                    }
                } else if (!this$valueAsString.equals(other$valueAsString)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof DefaultStringTuple;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        Object $valueAsString = this.getValueAsString();
        result = result * 59 + ($valueAsString == null ? 43 : $valueAsString.hashCode());
        return result;
    }
}
