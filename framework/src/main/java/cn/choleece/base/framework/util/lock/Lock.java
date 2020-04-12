package cn.choleece.base.framework.util.lock;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-03 15:39
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
