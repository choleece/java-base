package cn.choleece.base.springboot.mybatis.plugin.executor;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;

/**
 * @author choleece
 * @Description: 具体的加解密工厂
 * @Date 2020-09-13 11:15
 **/
public class CryptExecutorFactory {

    static final CryptExecutor PHONE_EXECUTOR = new PhoneCryptExecutor();
    static final CryptExecutor NAME_EXECUTOR = new NameCryptExecutor();
    static final CryptExecutor DEFAULT_EXECUTOR = new DefaultCryptExecutor();

    /**
     * 通过crypt type 返回具体的executor典型的工厂模式
     * @param cryptType
     * @return
     */
    public static CryptExecutor createCryptExecutor(CryptField cryptType) {
        switch (cryptType.value()) {
            case NAME:
                return NAME_EXECUTOR;
            case PHONE:
                return PHONE_EXECUTOR;
            default:
                return DEFAULT_EXECUTOR;
        }
    }

}
