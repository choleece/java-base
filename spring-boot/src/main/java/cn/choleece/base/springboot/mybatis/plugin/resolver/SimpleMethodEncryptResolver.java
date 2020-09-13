package cn.choleece.base.springboot.mybatis.plugin.resolver;

import cn.choleece.base.springboot.mybatis.plugin.handler.CryptHandlerFactory;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-13 21:53
 **/
public class SimpleMethodEncryptResolver implements MethodEncryptResolver {

    @Override
    public Object processEncrypt(Object param) {
        return CryptHandlerFactory.getCryptHandler(param, null).encrypt(param, null);
    }
}
