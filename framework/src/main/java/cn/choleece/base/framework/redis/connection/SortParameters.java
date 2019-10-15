package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 10:37
 */
public interface SortParameters {

    @Nullable
    SortParameters.Order getOrder();

    @Nullable
    Boolean isAlphabetic();

    @Nullable
    byte[] getByPattern();

    @Nullable
    byte[][] getGetPattern();

    @Nullable
    SortParameters.Range getLimit();

    public class Range {
        private final long start;
        private final long count;

        public Range(long start, long count) {
            this.start = start;
            this.count = count;
        }

        public long getStart() {
            return this.start;
        }

        public long getCount() {
            return this.count;
        }
    }

    public enum Order {
        ASC,
        DESC;

        private Order() {
        }
    }
}
