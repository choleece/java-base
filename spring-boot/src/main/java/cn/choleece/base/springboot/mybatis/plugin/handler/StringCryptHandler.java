package cn.choleece.base.springboot.mybatis.plugin.handler;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;
import cn.choleece.base.springboot.mybatis.plugin.executor.CryptExecutorFactory;

/**
 * @author choleece
 * @Description: String 类型的加解密
 * @Date 2020-09-13 14:08
 **/
public class StringCryptHandler implements CryptHandler<String> {

    @Override
    public Object encrypt(String param, CryptField cryptField) {
        if (needCrypt(param, cryptField)) {
            return CryptExecutorFactory.createCryptExecutor(cryptField).encrypt(param);
        }
        return param;
    }

    @Override
    public Object decrypt(String param, CryptField cryptField) {
        if (needCrypt(param, cryptField)) {
            return CryptExecutorFactory.createCryptExecutor(cryptField).decrypt(param);
        }
        return param;
    }

    private boolean needCrypt(String param, CryptField cryptField) {
        return cryptField != null && param != null && param.length() != 0;
    }
}
