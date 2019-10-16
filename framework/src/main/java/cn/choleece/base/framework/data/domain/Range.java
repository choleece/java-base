package cn.choleece.base.framework.data.domain;

import lombok.Generated;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 11:36
 */
public class Range<T extends Comparable<T>> {
    private static final Range<?> UNBOUNDED;
    @NonNull
    private final Range.Bound<T> lowerBound;
    @NonNull
    private final Range.Bound<T> upperBound;

    /** @deprecated */
    @Deprecated
    public Range(T lowerBound, T upperBound) {
        this(lowerBound, upperBound, true, true);
    }

    /** @deprecated */
    @Deprecated
    public Range(T lowerBound, T upperBound, boolean lowerInclusive, boolean upperInclusive) {
        this.lowerBound = lowerBound == null ? Range.Bound.unbounded() : (lowerInclusive ? Range.Bound.inclusive(lowerBound) : Range.Bound.exclusive(lowerBound));
        this.upperBound = upperBound == null ? Range.Bound.unbounded() : (upperInclusive ? Range.Bound.inclusive(upperBound) : Range.Bound.exclusive(upperBound));
    }

    public static <T extends Comparable<T>> Range<T> unbounded() {
        return (Range<T>) UNBOUNDED;
    }

    public static <T extends Comparable<T>> Range.RangeBuilder<T> from(Range.Bound<T> lower) {
        Assert.notNull(lower, "Lower bound must not be null!");
        return new Range.RangeBuilder(lower);
    }

    public static <T extends Comparable<T>> Range<T> of(Range.Bound<T> lowerBound, Range.Bound<T> upperBound) {
        return new Range(lowerBound, upperBound);
    }

    /** @deprecated */
    @Deprecated
    public boolean isLowerInclusive() {
        return this.lowerBound.isInclusive();
    }

    /** @deprecated */
    @Deprecated
    public boolean isUpperInclusive() {
        return this.upperBound.isInclusive();
    }

    public boolean contains(T value) {
        Assert.notNull(value, "Reference value must not be null!");
        boolean greaterThanLowerBound = (Boolean)this.lowerBound.getValue().map((it) -> {
            return this.lowerBound.isInclusive() ? it.compareTo(value) <= 0 : it.compareTo(value) < 0;
        }).orElse(true);
        boolean lessThanUpperBound = (Boolean)this.upperBound.getValue().map((it) -> {
            return this.upperBound.isInclusive() ? it.compareTo(value) >= 0 : it.compareTo(value) > 0;
        }).orElse(true);
        return greaterThanLowerBound && lessThanUpperBound;
    }

    public String toString() {
        return String.format("%s-%s", this.lowerBound.toPrefixString(), this.upperBound.toSuffixString());
    }

    @NonNull
    @Generated
    public Range.Bound<T> getLowerBound() {
        return this.lowerBound;
    }

    @NonNull
    @Generated
    public Range.Bound<T> getUpperBound() {
        return this.upperBound;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Range)) {
            return false;
        } else {
            Range<?> other = (Range)o;
            Object this$lowerBound = this.getLowerBound();
            Object other$lowerBound = other.getLowerBound();
            if (this$lowerBound == null) {
                if (other$lowerBound != null) {
                    return false;
                }
            } else if (!this$lowerBound.equals(other$lowerBound)) {
                return false;
            }

            Object this$upperBound = this.getUpperBound();
            Object other$upperBound = other.getUpperBound();
            if (this$upperBound == null) {
                if (other$upperBound != null) {
                    return false;
                }
            } else if (!this$upperBound.equals(other$upperBound)) {
                return false;
            }

            return true;
        }
    }

    @Override
    @Generated
    public int hashCode() {
        int result = 1;
        Object $lowerBound = this.getLowerBound();
        result = result * 59 + ($lowerBound == null ? 43 : $lowerBound.hashCode());
        Object $upperBound = this.getUpperBound();
        result = result * 59 + ($upperBound == null ? 43 : $upperBound.hashCode());
        return result;
    }

    @Generated
    private Range(@NonNull Range.Bound<T> lowerBound, @NonNull Range.Bound<T> upperBound) {
        if (lowerBound == null) {
            throw new IllegalArgumentException("lowerBound is marked @NonNull but is null");
        } else if (upperBound == null) {
            throw new IllegalArgumentException("upperBound is marked @NonNull but is null");
        } else {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
    }

    static {
        UNBOUNDED = of(Range.Bound.unbounded(), Range.Bound.UNBOUNDED);
    }

    public static class RangeBuilder<T extends Comparable<T>> {
        private final Range.Bound<T> lower;

        RangeBuilder(Range.Bound<T> lower) {
            this.lower = lower;
        }

        public Range<T> to(Range.Bound<T> upper) {
            Assert.notNull(upper, "Upper bound must not be null!");
            return new Range(this.lower, upper);
        }
    }

    public static final class Bound<T extends Comparable<T>> {
        private static final Range.Bound<?> UNBOUNDED = new Range.Bound(Optional.empty(), true);
        private final Optional<T> value;
        private final boolean inclusive;

        public static <T extends Comparable<T>> Range.Bound<T> unbounded() {
            return (Bound<T>) UNBOUNDED;
        }

        public boolean isBounded() {
            return this.value.isPresent();
        }

        public static <T extends Comparable<T>> Range.Bound<T> inclusive(T value) {
            Assert.notNull(value, "Value must not be null!");
            return new Range.Bound(Optional.of(value), true);
        }

        public static Range.Bound<Integer> inclusive(int value) {
            return inclusive(value);
        }

        public static Range.Bound<Long> inclusive(long value) {
            return inclusive(value);
        }

        public static Range.Bound<Float> inclusive(float value) {
            return inclusive(value);
        }

        public static Range.Bound<Double> inclusive(double value) {
            return inclusive(value);
        }

        public static <T extends Comparable<T>> Range.Bound<T> exclusive(T value) {
            Assert.notNull(value, "Value must not be null!");
            return new Range.Bound(Optional.of(value), false);
        }

        public static Range.Bound<Integer> exclusive(int value) {
            return exclusive(value);
        }

        public static Range.Bound<Long> exclusive(long value) {
            return exclusive(value);
        }

        public static Range.Bound<Float> exclusive(float value) {
            return exclusive(value);
        }

        public static Range.Bound<Double> exclusive(double value) {
            return exclusive(value);
        }

        String toPrefixString() {
            return (String)this.getValue().map(Object::toString).map((it) -> {
                return this.isInclusive() ? "[".concat(it) : "(".concat(it);
            }).orElse("unbounded");
        }

        String toSuffixString() {
            return (String)this.getValue().map(Object::toString).map((it) -> {
                return this.isInclusive() ? it.concat("]") : it.concat(")");
            }).orElse("unbounded");
        }

        @Override
        public String toString() {
            return (String)this.value.map(Object::toString).orElse("unbounded");
        }

        @Generated
        public Optional<T> getValue() {
            return this.value;
        }

        @Generated
        public boolean isInclusive() {
            return this.inclusive;
        }

        @Override
        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof Range.Bound)) {
                return false;
            } else {
                Range.Bound<?> other = (Range.Bound)o;
                Object this$value = this.getValue();
                Object other$value = other.getValue();
                if (this$value == null) {
                    if (other$value == null) {
                        return this.isInclusive() == other.isInclusive();
                    }
                } else if (this$value.equals(other$value)) {
                    return this.isInclusive() == other.isInclusive();
                }

                return false;
            }
        }

        @Override
        @Generated
        public int hashCode() {
            int result = 1;
            Object $value = this.getValue();
            result = result * 59 + ($value == null ? 43 : $value.hashCode());
            result = result * 59 + (this.isInclusive() ? 79 : 97);
            return result;
        }

        @Generated
        private Bound(Optional<T> value, boolean inclusive) {
            this.value = value;
            this.inclusive = inclusive;
        }
    }
}
