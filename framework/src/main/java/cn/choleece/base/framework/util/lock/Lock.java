package cn.choleece.base.framework.util.lock;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-03 15:39
 **/
public interface Lock {

    Boolean lock(String randomId);

    Boolean unlock(String randomId);

}
