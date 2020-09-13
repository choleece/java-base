package cn.choleece.base.springboot.mybatis.plugin.executor;

/**
 * @author choleece
 * @Description: 电话号码加密
 * @Date 2020-09-13 13:46
 **/
public class PhoneCryptExecutor implements CryptExecutor {

    @Override
    public String encrypt(String source) {
        System.out.println("电话号加密");
        return source;
    }

    @Override
    public String decrypt(String source) {
        System.out.println("电话号解密");
        return source;
    }
}
