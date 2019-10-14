package cn.choleece.base.framework.redis.core;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:38
 **/
public class ScanOptions {

    public static ScanOptions NONE = new ScanOptions((Long)null, (String)null);
    @Nullable
    private final Long count;
    @Nullable
    private final String pattern;

    private ScanOptions(@Nullable Long count, @Nullable String pattern) {
        this.count = count;
        this.pattern = pattern;
    }

    public static ScanOptions.ScanOptionsBuilder scanOptions() {
        return new ScanOptions.ScanOptionsBuilder();
    }

    @Nullable
    public Long getCount() {
        return this.count;
    }

    @Nullable
    public String getPattern() {
        return this.pattern;
    }

    public String toOptionString() {
        if (this.equals(NONE)) {
            return "";
        } else {
            String params = "";
            if (this.count != null) {
                params = params + ", 'count', " + this.count;
            }

            if (StringUtils.hasText(this.pattern)) {
                params = params + ", 'match' , '" + this.pattern + "'";
            }

            return params;
        }
    }

    public static class ScanOptionsBuilder {
        @Nullable
        private Long count;
        @Nullable
        private String pattern;

        public ScanOptionsBuilder() {
        }

        public ScanOptions.ScanOptionsBuilder count(long count) {
            this.count = count;
            return this;
        }

        public ScanOptions.ScanOptionsBuilder match(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public ScanOptions build() {
            return new ScanOptions(this.count, this.pattern);
        }
    }
}
