package cn.choleece.base.springboot.mybatis.plugin.executor;

/**
 * @author choleece
 * @Description: 默认的加解密
 * @Date 2020-09-13 13:49
 **/
public class DefaultCryptExecutor implements CryptExecutor {

    @Override
    public String encrypt(String source) {
        return source;
    }

    @Override
    public String decrypt(String source) {
        return source;
    }
}
