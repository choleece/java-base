package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 10:45
 */
public class BitFieldSubCommands implements Iterable<BitFieldSubCommands.BitFieldSubCommand> {
    private final List<BitFieldSubCommand> subCommands;

    private BitFieldSubCommands(List<BitFieldSubCommands.BitFieldSubCommand> subCommands) {
        this.subCommands = new ArrayList(subCommands);
    }

    private BitFieldSubCommands(List<BitFieldSubCommands.BitFieldSubCommand> subCommands, BitFieldSubCommands.BitFieldSubCommand subCommand) {
        this(subCommands);
        Assert.notNull(subCommand, "SubCommand must not be null!");
        this.subCommands.add(subCommand);
    }

    public static BitFieldSubCommands create() {
        return new BitFieldSubCommands(Collections.emptyList());
    }

    public BitFieldSubCommands.BitFieldGetBuilder get(BitFieldSubCommands.BitFieldType type) {
        return (new BitFieldSubCommands.BitFieldGetBuilder(this)).forType(type);
    }

    protected BitFieldSubCommands get(BitFieldSubCommands.BitFieldGet get) {
        return new BitFieldSubCommands(this.subCommands, get);
    }

    public BitFieldSubCommands.BitFieldSetBuilder set(BitFieldSubCommands.BitFieldType type) {
        return (new BitFieldSubCommands.BitFieldSetBuilder(this)).forType(type);
    }

    protected BitFieldSubCommands set(BitFieldSubCommands.BitFieldSet set) {
        return new BitFieldSubCommands(this.subCommands, set);
    }

    public BitFieldSubCommands.BitFieldIncrByBuilder incr(BitFieldSubCommands.BitFieldType type) {
        return (new BitFieldSubCommands.BitFieldIncrByBuilder(this)).forType(type);
    }

    protected BitFieldSubCommands incr(BitFieldSubCommands.BitFieldIncrBy incrBy) {
        return new BitFieldSubCommands(this.subCommands, incrBy);
    }

    public List<BitFieldSubCommands.BitFieldSubCommand> getSubCommands() {
        return this.subCommands;
    }

    public Iterator<BitFieldSubCommand> iterator() {
        return this.subCommands.iterator();
    }

    public static class BitFieldIncrBy extends BitFieldSubCommands.AbstractBitFieldSubCommand {
        private long value;
        @Nullable
        private BitFieldSubCommands.BitFieldIncrBy.Overflow overflow;

        public BitFieldIncrBy() {
        }

        public String getCommand() {
            return "INCRBY";
        }

        public long getValue() {
            return this.value;
        }

        @Nullable
        public BitFieldSubCommands.BitFieldIncrBy.Overflow getOverflow() {
            return this.overflow;
        }

        public static enum Overflow {
            SAT,
            FAIL,
            WRAP;

            private Overflow() {
            }
        }
    }

    public static class BitFieldGet extends BitFieldSubCommands.AbstractBitFieldSubCommand {
        public BitFieldGet() {
        }

        public String getCommand() {
            return "GET";
        }
    }

    public static class BitFieldSet extends BitFieldSubCommands.AbstractBitFieldSubCommand {
        private long value;

        public BitFieldSet() {
        }

        public String getCommand() {
            return "SET";
        }

        public long getValue() {
            return this.value;
        }
    }

    public abstract static class AbstractBitFieldSubCommand implements BitFieldSubCommands.BitFieldSubCommand {
        BitFieldSubCommands.BitFieldType type;
        BitFieldSubCommands.Offset offset;

        public AbstractBitFieldSubCommand() {
        }

        public BitFieldSubCommands.BitFieldType getType() {
            return this.type;
        }

        public BitFieldSubCommands.Offset getOffset() {
            return this.offset;
        }
    }

    public static class BitFieldType {
        public static final BitFieldSubCommands.BitFieldType INT_8 = new BitFieldSubCommands.BitFieldType(true, 8);
        public static final BitFieldSubCommands.BitFieldType INT_16 = new BitFieldSubCommands.BitFieldType(true, 16);
        public static final BitFieldSubCommands.BitFieldType INT_32 = new BitFieldSubCommands.BitFieldType(true, 32);
        public static final BitFieldSubCommands.BitFieldType INT_64 = new BitFieldSubCommands.BitFieldType(true, 64);
        public static final BitFieldSubCommands.BitFieldType UINT_8 = new BitFieldSubCommands.BitFieldType(false, 8);
        public static final BitFieldSubCommands.BitFieldType UINT_16 = new BitFieldSubCommands.BitFieldType(false, 16);
        public static final BitFieldSubCommands.BitFieldType UINT_32 = new BitFieldSubCommands.BitFieldType(false, 32);
        public static final BitFieldSubCommands.BitFieldType UINT_64 = new BitFieldSubCommands.BitFieldType(false, 64);
        private final boolean signed;
        private final int bits;

        private BitFieldType(boolean signed, Integer bits) {
            this.signed = signed;
            this.bits = bits;
        }

        public static BitFieldSubCommands.BitFieldType signed(int bits) {
            return new BitFieldSubCommands.BitFieldType(true, bits);
        }

        public static BitFieldSubCommands.BitFieldType unsigned(int bits) {
            return new BitFieldSubCommands.BitFieldType(false, bits);
        }

        public boolean isSigned() {
            return this.signed;
        }

        public int getBits() {
            return this.bits;
        }

        public String asString() {
            return (this.isSigned() ? "i" : "u") + this.getBits();
        }

        public String toString() {
            return this.asString();
        }
    }

    public static class Offset {
        private final long offset;
        private final boolean zeroBased;

        private Offset(long offset, boolean zeroBased) {
            this.offset = offset;
            this.zeroBased = zeroBased;
        }

        public static BitFieldSubCommands.Offset offset(long offset) {
            return new BitFieldSubCommands.Offset(offset, true);
        }

        public BitFieldSubCommands.Offset multipliedByTypeLength() {
            return new BitFieldSubCommands.Offset(this.offset, false);
        }

        public boolean isZeroBased() {
            return this.zeroBased;
        }

        public long getValue() {
            return this.offset;
        }

        public String asString() {
            return (this.isZeroBased() ? "" : "#") + this.getValue();
        }

        public String toString() {
            return this.asString();
        }
    }

    public interface BitFieldSubCommand {
        String getCommand();

        BitFieldSubCommands.BitFieldType getType();

        BitFieldSubCommands.Offset getOffset();
    }

    public class BitFieldIncrByBuilder {
        private BitFieldSubCommands ref;
        BitFieldSubCommands.BitFieldIncrBy incrBy;

        private BitFieldIncrByBuilder(BitFieldSubCommands ref) {
            this.incrBy = new BitFieldSubCommands.BitFieldIncrBy();
            this.ref = ref;
        }

        public BitFieldSubCommands.BitFieldIncrByBuilder forType(BitFieldSubCommands.BitFieldType type) {
            this.incrBy.type = type;
            return this;
        }

        public BitFieldSubCommands.BitFieldIncrByBuilder valueAt(long offset) {
            return this.valueAt(BitFieldSubCommands.Offset.offset(offset));
        }

        public BitFieldSubCommands.BitFieldIncrByBuilder valueAt(BitFieldSubCommands.Offset offset) {
            Assert.notNull(offset, "Offset must not be null!");
            this.incrBy.offset = offset;
            return this;
        }

        public BitFieldSubCommands.BitFieldIncrByBuilder overflow(BitFieldSubCommands.BitFieldIncrBy.Overflow overflow) {
            this.incrBy.overflow = overflow;
            return this;
        }

        public BitFieldSubCommands by(long value) {
            this.incrBy.value = value;
            return this.ref.incr(this.incrBy);
        }
    }

    public class BitFieldGetBuilder {
        private BitFieldSubCommands ref;
        BitFieldSubCommands.BitFieldGet get;

        private BitFieldGetBuilder(BitFieldSubCommands ref) {
            this.get = new BitFieldSubCommands.BitFieldGet();
            this.ref = ref;
        }

        public BitFieldSubCommands.BitFieldGetBuilder forType(BitFieldSubCommands.BitFieldType type) {
            this.get.type = type;
            return this;
        }

        public BitFieldSubCommands valueAt(long offset) {
            return this.valueAt(BitFieldSubCommands.Offset.offset(offset));
        }

        public BitFieldSubCommands valueAt(BitFieldSubCommands.Offset offset) {
            Assert.notNull(offset, "Offset must not be null!");
            this.get.offset = offset;
            return this.ref.get(this.get);
        }
    }

    public static class BitFieldSetBuilder {
        private BitFieldSubCommands ref;
        BitFieldSubCommands.BitFieldSet set;

        private BitFieldSetBuilder(BitFieldSubCommands ref) {
            this.set = new BitFieldSubCommands.BitFieldSet();
            this.ref = ref;
        }

        public BitFieldSubCommands.BitFieldSetBuilder forType(BitFieldSubCommands.BitFieldType type) {
            this.set.type = type;
            return this;
        }

        public BitFieldSubCommands.BitFieldSetBuilder valueAt(long offset) {
            return this.valueAt(BitFieldSubCommands.Offset.offset(offset));
        }

        public BitFieldSubCommands.BitFieldSetBuilder valueAt(BitFieldSubCommands.Offset offset) {
            Assert.notNull(offset, "Offset must not be null!");
            this.set.offset = offset;
            return this;
        }

        public BitFieldSubCommands to(long value) {
            this.set.value = value;
            return this.ref.set(this.set);
        }
    }
}
