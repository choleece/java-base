package cn.choleece.base.springboot.mybatis.plugin.handler;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;

/**
 * @author choleece
 * @Description: 什么都不出处理
 * @Date 2020-09-13 14:08
 **/
public class EmptyCryptHandler implements CryptHandler<String> {

    @Override
    public Object encrypt(String param, CryptField cryptField) {
        return param;
    }

    @Override
    public Object decrypt(String param, CryptField cryptField) {
        return param;
    }
}
