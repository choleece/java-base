package cn.choleece.base.springboot.retry;

import cn.choleece.base.springboot.retry.exception.OneTypeException;
import cn.choleece.base.springboot.retry.exception.TwoTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author choleece
 * @Description: test spring retry
 * @Date 2020-09-07 21:53
 **/
@Service
public class RetryService {
    private static final Logger log = LoggerFactory.getLogger(RetryService.class);

    Integer counter = 0;

    @Retryable(value = { OneTypeException.class, TwoTypeException.class }, maxAttempts = 3, backoff = @Backoff(2000))
    public String retryIfException() throws OneTypeException, TwoTypeException {

        log.info("try times {}", counter);
        counter++;

        if (counter == 0) {
            throw new OneTypeException("exception one");
        } else if (counter == 1) {
            throw new TwoTypeException("exception two");
        } else {
            throw new RuntimeException();
        }
    }

    @Recover
    public String recover(Throwable t) {
        log.info("SampleRetryService.recover");
        return "Error Class :: " + t.getClass().getName();
    }



}
