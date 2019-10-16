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

    /**
     * Get the {@link ValueEncoding} for given {@code encoding}.
     *
     * @param encoding can be {@literal null}.
     * @return never {@literal null}.
     */
    static ValueEncoding of(@Nullable String encoding) {
        return RedisValueEncoding.lookup(encoding).orElse(() -> encoding);
    }

    /**
     * Default {@link ValueEncoding} implementation of encodings used in Redis.
     *
     * @author Christoph Strobl
     * @since 2.1
     */
    enum RedisValueEncoding implements ValueEncoding {

        /**
         * Normal string encoding.
         */
        RAW("raw"), //
        /**
         * 64 bit signed interval String representing an integer.
         */
        INT("int"), //
        /**
         * Space saving representation for small lists, hashes and sorted sets.
         */
        ZIPLIST("ziplist"), //
        /**
         * Encoding for large lists.
         */
        LINKEDLIST("linkedlist"), //
        /**
         * Space saving representation for small sets that contain only integers.ø
         */
        INTSET("intset"), //
        /**
         * Encoding for large hashes.
         */
        HASHTABLE("hashtable"), //
        /**
         * Encoding for sorted sets of any size.
         */
        SKIPLIST("skiplist"), //
        /**
         * No encoding present due to non existing key.
         */
        VACANT(null);

        private final @Nullable String raw;

        RedisValueEncoding(@Nullable String raw) {
            this.raw = raw;
        }

        @Override
        public String raw() {
            return raw;
        }

        @Nullable
        static Optional<ValueEncoding> lookup(@Nullable String encoding) {

            for (ValueEncoding valueEncoding : values()) {
                if (ObjectUtils.nullSafeEquals(valueEncoding.raw(), encoding)) {
                    return Optional.of(valueEncoding);
                }
            }
            return Optional.empty();
        }
    }
}
