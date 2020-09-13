package cn.choleece.base.springboot.mybatis.plugin.executor;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-13 13:45
 **/
public class NameCryptExecutor implements CryptExecutor {

    @Override
    public String encrypt(String source) {
        System.out.println("根据姓名加密");
        return source;
    }

    @Override
    public String decrypt(String source) {
        System.out.println("根据姓名解密");
        return source;
    }
}
