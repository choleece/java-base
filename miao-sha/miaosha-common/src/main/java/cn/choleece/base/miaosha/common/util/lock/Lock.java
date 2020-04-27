package cn.choleece.base.miaosha.common.util.lock;

/**
 * @author choleece
 * @Description: 锁
 * @Date 2020-04-27 23:14
 **/
public interface Lock {

    /**
     * 获取锁
     * @param randomId
     * @return
     */
    Boolean lock(String randomId);

    /**
     * 释放锁
     * @param randomId
     * @return
     */
    Boolean unlock(String randomId);

}
