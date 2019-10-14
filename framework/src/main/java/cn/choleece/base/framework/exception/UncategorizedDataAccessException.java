package cn.choleece.base.framework.exception;

import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:39
 */
public class UncategorizedDataAccessException extends NonTransientDataAccessException {
    public UncategorizedDataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
