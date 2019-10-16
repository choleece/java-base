package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.exception.InvalidDataAccessResourceUsageException;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:13
 **/
public class RedisPipelineException extends InvalidDataAccessResourceUsageException {
    private final List<Object> results;

    public RedisPipelineException(@Nullable String msg, @Nullable Throwable cause, List<Object> pipelineResult) {
        super(msg, cause);
        this.results = Collections.unmodifiableList(pipelineResult);
    }

    public RedisPipelineException(Exception cause, List<Object> pipelineResult) {
        this("Pipeline contained one or more invalid commands", cause, pipelineResult);
    }

    public RedisPipelineException(Exception cause) {
        this("Pipeline contained one or more invalid commands", cause, Collections.emptyList());
    }

    public RedisPipelineException(String msg, List<Object> pipelineResult) {
        super(msg);
        this.results = Collections.unmodifiableList(pipelineResult);
    }

    public List<Object> getPipelineResult() {
        return this.results;
    }
}
