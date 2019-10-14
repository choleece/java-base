package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.exception.DataAccessException;
import org.springframework.lang.Nullable;

/**
 * @description: redis 异常翻译策略 这里用到策略模式（设计模式部分可以参考design-pattern部分）
 * @author: sf
 * @time: 2019-10-14 17:33
 */
public interface ExceptionTranslationStrategy {

    @Nullable
    DataAccessException translate(Exception var1);

}
