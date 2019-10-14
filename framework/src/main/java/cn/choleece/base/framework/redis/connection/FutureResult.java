package cn.choleece.base.framework.redis.connection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.util.function.Supplier;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:19
 */
public abstract class FutureResult<T> {

    private T resultHolder;
    private final Supplier<?> defaultConversionResult;
    private boolean status;
    protected Converter converter;

    public FutureResult(T resultHolder) {
        this(resultHolder, (val) -> {
            return val;
        });
    }

    public FutureResult(T resultHolder, @Nullable Converter converter) {
        this(resultHolder, converter, () -> {
            return null;
        });
    }

    public FutureResult(T resultHolder, @Nullable Converter converter, Supplier<?> defaultConversionResult) {
        this.status = false;
        this.resultHolder = resultHolder;
        this.converter = converter != null ? converter : (val) -> {
            return val;
        };
        this.defaultConversionResult = defaultConversionResult;
    }

    public T getResultHolder() {
        return this.resultHolder;
    }

    @Nullable
    public Object convert(@Nullable Object result) {
        return result == null ? this.computeDefaultResult((Object)null) : this.computeDefaultResult(this.converter.convert(result));
    }

    @Nullable
    private Object computeDefaultResult(@Nullable Object source) {
        return source != null ? source : this.defaultConversionResult.get();
    }

    public Converter getConverter() {
        return this.converter;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Nullable
    public abstract Object get();

    public abstract boolean conversionRequired();

}
