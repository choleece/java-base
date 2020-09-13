package cn.choleece.base.springboot.mybatis.plugin.handler;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;

/**
 * @author choleece
 * @Description: 加解密handler
 * @Date 2020-09-13 13:53
 **/
public interface CryptHandler<T> {

    /**
     * 加密
     * @param param param
     * @param cryptField encrypt field
     * @return after encrypt
     */
    Object encrypt(T param, CryptField cryptField);

    /**
     * 解密
     * @param param param
     * @param cryptField decrypt field
     * @return after decrypt
     */
    Object decrypt(T param, CryptField cryptField);

}
