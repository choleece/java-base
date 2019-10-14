package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.redis.connection.FutureResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import redis.clients.jedis.Response;

import java.util.function.Supplier;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:18
 */
public class JedisResult<T, R> extends FutureResult<Response<?>> {

    private final boolean convertPipelineAndTxResults;

    JedisResult(Response<T> resultHolder) {
        this(resultHolder, false, (Converter)null);
    }

    JedisResult(Response<T> resultHolder, boolean convertPipelineAndTxResults, @Nullable Converter<T, ?> converter) {
        this(resultHolder, () -> {
            return null;
        }, convertPipelineAndTxResults, converter);
    }

    JedisResult(Response<T> resultHolder, Supplier<R> defaultReturnValue, boolean convertPipelineAndTxResults, @Nullable Converter<T, ?> converter) {
        super(resultHolder, converter, defaultReturnValue);
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    @Nullable
    @Override
    public T get() {
        return ((Response)this.getResultHolder()).get();
    }

    @Override
    public boolean conversionRequired() {
        return this.convertPipelineAndTxResults;
    }

    static class JedisResultBuilder<T, R> {
        private final Response<T> response;
        private Converter<T, R> converter;
        private boolean convertPipelineAndTxResults = false;
        private Supplier<R> nullValueDefault = () -> {
            return null;
        };

        JedisResultBuilder(Response<T> response) {
            this.response = response;
            this.converter = (source) -> {
                return source;
            };
        }

        static <T, R> JedisResult.JedisResultBuilder<T, R> forResponse(Response<T> response) {
            return new JedisResult.JedisResultBuilder(response);
        }

        JedisResult.JedisResultBuilder<T, R> mappedWith(Converter<T, R> converter) {
            this.converter = converter;
            return this;
        }

        JedisResult.JedisResultBuilder<T, R> mapNullTo(Supplier<R> supplier) {
            this.nullValueDefault = supplier;
            return this;
        }

        JedisResult.JedisResultBuilder<T, R> convertPipelineAndTxResults(boolean flag) {
            this.convertPipelineAndTxResults = flag;
            return this;
        }

        JedisResult<T, R> build() {
            return new JedisResult(this.response, this.nullValueDefault, this.convertPipelineAndTxResults, this.converter);
        }

        JedisResult.JedisStatusResult buildStatusResult() {
            return new JedisResult.JedisStatusResult(this.response, this.converter);
        }
    }

    static class JedisStatusResult<T, R> extends JedisResult<T, R> {
        JedisStatusResult(Response<T> resultHolder, Converter<T, R> converter) {
            super(resultHolder, false, converter);
            this.setStatus(true);
        }
    }
}
