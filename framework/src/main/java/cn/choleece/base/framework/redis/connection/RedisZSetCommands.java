package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * @author choleece
 * @Description: ZSet 命令
 * @Date 2019-10-14 22:39
 **/
public interface RedisZSetCommands {

    @Nullable
    Boolean zAdd(byte[] var1, double var2, byte[] var4);

    @Nullable
    Long zAdd(byte[] var1, Set<Tuple> var2);

    @Nullable
    Long zRem(byte[] var1, byte[]... var2);

    @Nullable
    Double zIncrBy(byte[] var1, double var2, byte[] var4);

    @Nullable
    Long zRank(byte[] var1, byte[] var2);

    @Nullable
    Long zRevRank(byte[] var1, byte[] var2);

    @Nullable
    Set<byte[]> zRange(byte[] var1, long var2, long var4);

    @Nullable
    Set<RedisZSetCommands.Tuple> zRangeWithScores(byte[] var1, long var2, long var4);

    @Nullable
    default Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        return this.zRangeByScore(key, (new RedisZSetCommands.Range()).gte(min).lte(max));
    }

    @Nullable
    default Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return this.zRangeByScoreWithScores(key, range, RedisZSetCommands.Limit.unlimited());
    }

    @Nullable
    default Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        return this.zRangeByScoreWithScores(key, (new RedisZSetCommands.Range()).gte(min).lte(max));
    }

    @Nullable
    default Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return this.zRangeByScore(key, (new RedisZSetCommands.Range()).gte(min).lte(max), (new RedisZSetCommands.Limit()).offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Nullable
    default Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return this.zRangeByScoreWithScores(key, (new RedisZSetCommands.Range()).gte(min).lte(max), (new RedisZSetCommands.Limit()).offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Nullable
    Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] var1, RedisZSetCommands.Range var2, RedisZSetCommands.Limit var3);

    @Nullable
    Set<byte[]> zRevRange(byte[] var1, long var2, long var4);

    @Nullable
    Set<RedisZSetCommands.Tuple> zRevRangeWithScores(byte[] var1, long var2, long var4);

    @Nullable
    default Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return this.zRevRangeByScore(key, (new RedisZSetCommands.Range()).gte(min).lte(max));
    }

    @Nullable
    default Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return this.zRevRangeByScore(key, range, RedisZSetCommands.Limit.unlimited());
    }

    @Nullable
    default Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return this.zRevRangeByScoreWithScores(key, (new RedisZSetCommands.Range()).gte(min).lte(max), RedisZSetCommands.Limit.unlimited());
    }

    @Nullable
    default Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return this.zRevRangeByScore(key, (new RedisZSetCommands.Range()).gte(min).lte(max), (new RedisZSetCommands.Limit()).offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Nullable
    Set<byte[]> zRevRangeByScore(byte[] var1, RedisZSetCommands.Range var2, RedisZSetCommands.Limit var3);

    @Nullable
    default Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return this.zRevRangeByScoreWithScores(key, (new RedisZSetCommands.Range()).gte(min).lte(max), (new RedisZSetCommands.Limit()).offset(Long.valueOf(offset).intValue()).count(Long.valueOf(count).intValue()));
    }

    @Nullable
    default Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return this.zRevRangeByScoreWithScores(key, range, RedisZSetCommands.Limit.unlimited());
    }

    @Nullable
    Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] var1, RedisZSetCommands.Range var2, RedisZSetCommands.Limit var3);

    @Nullable
    default Long zCount(byte[] key, double min, double max) {
        return this.zCount(key, (new RedisZSetCommands.Range()).gte(min).lte(max));
    }

    @Nullable
    Long zCount(byte[] var1, RedisZSetCommands.Range var2);

    @Nullable
    Long zCard(byte[] var1);

    @Nullable
    Double zScore(byte[] var1, byte[] var2);

    @Nullable
    Long zRemRange(byte[] var1, long var2, long var4);

    @Nullable
    default Long zRemRangeByScore(byte[] key, double min, double max) {
        return this.zRemRangeByScore(key, (new RedisZSetCommands.Range()).gte(min).lte(max));
    }

    @Nullable
    Long zRemRangeByScore(byte[] var1, RedisZSetCommands.Range var2);

    @Nullable
    Long zUnionStore(byte[] var1, byte[]... var2);

    @Nullable
    default Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        return this.zUnionStore(destKey, aggregate, RedisZSetCommands.Weights.of(weights), sets);
    }

    @Nullable
    Long zUnionStore(byte[] var1, RedisZSetCommands.Aggregate var2, RedisZSetCommands.Weights var3, byte[]... var4);

    @Nullable
    Long zInterStore(byte[] var1, byte[]... var2);

    @Nullable
    default Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        return this.zInterStore(destKey, aggregate, RedisZSetCommands.Weights.of(weights), sets);
    }

    @Nullable
    Long zInterStore(byte[] var1, RedisZSetCommands.Aggregate var2, RedisZSetCommands.Weights var3, byte[]... var4);

    Cursor<RedisZSetCommands.Tuple> zScan(byte[] var1, ScanOptions var2);

    @Nullable
    default Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        return this.zRangeByScore(key, (new RedisZSetCommands.Range()).gte(min).lte(max));
    }

    @Nullable
    default Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return this.zRangeByScore(key, range, RedisZSetCommands.Limit.unlimited());
    }

    @Nullable
    Set<byte[]> zRangeByScore(byte[] var1, String var2, String var3, long var4, long var6);

    @Nullable
    Set<byte[]> zRangeByScore(byte[] var1, RedisZSetCommands.Range var2, RedisZSetCommands.Limit var3);

    @Nullable
    default Set<byte[]> zRangeByLex(byte[] key) {
        return this.zRangeByLex(key, RedisZSetCommands.Range.unbounded());
    }

    @Nullable
    default Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range) {
        return this.zRangeByLex(key, range, RedisZSetCommands.Limit.unlimited());
    }

    @Nullable
    Set<byte[]> zRangeByLex(byte[] var1, RedisZSetCommands.Range var2, RedisZSetCommands.Limit var3);

    public static class Limit {
        private static final RedisZSetCommands.Limit UNLIMITED = new RedisZSetCommands.Limit() {
            public int getCount() {
                return -1;
            }

            public int getOffset() {
                return super.getOffset();
            }
        };
        int offset;
        int count;

        public Limit() {
        }

        public static RedisZSetCommands.Limit limit() {
            return new RedisZSetCommands.Limit();
        }

        public RedisZSetCommands.Limit offset(int offset) {
            this.offset = offset;
            return this;
        }

        public RedisZSetCommands.Limit count(int count) {
            this.count = count;
            return this;
        }

        public int getCount() {
            return this.count;
        }

        public int getOffset() {
            return this.offset;
        }

        public boolean isUnlimited() {
            return this.equals(UNLIMITED);
        }

        public static RedisZSetCommands.Limit unlimited() {
            return UNLIMITED;
        }
    }

    public static class Range {
        @Nullable
        RedisZSetCommands.Range.Boundary min;
        @Nullable
        RedisZSetCommands.Range.Boundary max;

        public Range() {
        }

        public static RedisZSetCommands.Range range() {
            return new RedisZSetCommands.Range();
        }

        public static RedisZSetCommands.Range unbounded() {
            RedisZSetCommands.Range range = new RedisZSetCommands.Range();
            range.min = RedisZSetCommands.Range.Boundary.infinite();
            range.max = RedisZSetCommands.Range.Boundary.infinite();
            return range;
        }

        public RedisZSetCommands.Range gte(Object min) {
            Assert.notNull(min, "Min already set for range.");
            this.min = new RedisZSetCommands.Range.Boundary(min, true);
            return this;
        }

        public RedisZSetCommands.Range gt(Object min) {
            Assert.notNull(min, "Min already set for range.");
            this.min = new RedisZSetCommands.Range.Boundary(min, false);
            return this;
        }

        public RedisZSetCommands.Range lte(Object max) {
            Assert.notNull(max, "Max already set for range.");
            this.max = new RedisZSetCommands.Range.Boundary(max, true);
            return this;
        }

        public RedisZSetCommands.Range lt(Object max) {
            Assert.notNull(max, "Max already set for range.");
            this.max = new RedisZSetCommands.Range.Boundary(max, false);
            return this;
        }

        @Nullable
        public RedisZSetCommands.Range.Boundary getMin() {
            return this.min;
        }

        @Nullable
        public RedisZSetCommands.Range.Boundary getMax() {
            return this.max;
        }

        public static class Boundary {
            @Nullable
            Object value;
            boolean including;

            static RedisZSetCommands.Range.Boundary infinite() {
                return new RedisZSetCommands.Range.Boundary((Object)null, true);
            }

            Boundary(@Nullable Object value, boolean including) {
                this.value = value;
                this.including = including;
            }

            @Nullable
            public Object getValue() {
                return this.value;
            }

            public boolean isIncluding() {
                return this.including;
            }
        }
    }

    public interface Tuple extends Comparable<Double> {
        byte[] getValue();

        Double getScore();
    }

    public static class Weights {
        private final List<Double> weights;

        private Weights(List<Double> weights) {
            this.weights = weights;
        }

        public static RedisZSetCommands.Weights of(int... weights) {
            Assert.notNull(weights, "Weights must not be null!");
            return new RedisZSetCommands.Weights((List) Arrays.stream(weights).mapToDouble((value) -> {
                return (double)value;
            }).boxed().collect(Collectors.toList()));
        }

        public static RedisZSetCommands.Weights of(double... weights) {
            Assert.notNull(weights, "Weights must not be null!");
            return new RedisZSetCommands.Weights((List) DoubleStream.of(weights).boxed().collect(Collectors.toList()));
        }

        public static RedisZSetCommands.Weights fromSetCount(int count) {
            Assert.isTrue(count >= 0, "Count of input sorted sets must be greater or equal to zero!");
            return new RedisZSetCommands.Weights((List) IntStream.range(0, count).mapToDouble((value) -> {
                return 1.0D;
            }).boxed().collect(Collectors.toList()));
        }

        public RedisZSetCommands.Weights multiply(int multiplier) {
            return this.apply((it) -> {
                return it * (double)multiplier;
            });
        }

        public RedisZSetCommands.Weights multiply(double multiplier) {
            return this.apply((it) -> {
                return it * multiplier;
            });
        }

        public RedisZSetCommands.Weights apply(Function<Double, Double> operator) {
            return new RedisZSetCommands.Weights((List)this.weights.stream().map(operator).collect(Collectors.toList()));
        }

        public double getWeight(int index) {
            return (Double)this.weights.get(index);
        }

        public int size() {
            return this.weights.size();
        }

        public double[] toArray() {
            return this.weights.stream().mapToDouble(Double::doubleValue).toArray();
        }

        public List<Double> toList() {
            return Collections.unmodifiableList(this.weights);
        }
    }

    public static enum Aggregate {
        SUM,
        MIN,
        MAX;

        private Aggregate() {
        }
    }
}
