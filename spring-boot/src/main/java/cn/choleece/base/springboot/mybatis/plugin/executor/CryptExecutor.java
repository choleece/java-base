package cn.choleece.base.springboot.mybatis.plugin.executor;

/**
 * @author choleece
 * @Description: 加解密抽象
 * @Date 2020-09-13 10:49
 **/
public interface CryptExecutor {

    /**
     * 加密
     * @param source encrypt source str
     * @return encrypt str
     */
    String encrypt(String source);

    /**
     * 揭秘
     * @param source decrypt source str
     * @return decrypt str
     */
    String decrypt(String source);

}
