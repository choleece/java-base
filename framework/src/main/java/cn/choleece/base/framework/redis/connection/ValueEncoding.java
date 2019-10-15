package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * @description: Value 编码
 * @author: sf
 * @time: 2019-10-15 09:36
 */
public interface ValueEncoding {

    @Nullable
    String raw();

    static ValueEncoding of(@Nullable String encoding) {
        return (ValueEncoding)ValueEncoding.RedisValueEncoding.lookup(encoding).orElse(() -> {
            return encoding;
        });
    }

    public enum RedisValueEncoding implements ValueEncoding {
        RAW("raw"),
        INT("int"),
        ZIPLIST("ziplist"),
        LINKEDLIST("linkedlist"),
        INTSET("intset"),
        HASHTABLE("hashtable"),
        SKIPLIST("skiplist"),
        VACANT((String)null);

        @Nullable
        private final String raw;

        private RedisValueEncoding(@Nullable String raw) {
            this.raw = raw;
        }

        public String raw() {
            return this.raw;
        }

        @Nullable
        static Optional<ValueEncoding> lookup(@Nullable String encoding) {
            ValueEncoding.RedisValueEncoding[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ValueEncoding valueEncoding = var1[var3];
                if (ObjectUtils.nullSafeEquals(valueEncoding.raw(), encoding)) {
                    return Optional.of(valueEncoding);
                }
            }

            return Optional.empty();
        }
    }
}
