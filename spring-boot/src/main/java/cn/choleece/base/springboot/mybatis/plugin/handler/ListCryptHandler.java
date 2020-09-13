package cn.choleece.base.springboot.mybatis.plugin.handler;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author choleece
 * @Description: List 类型的加解密
 * @Date 2020-09-13 14:08
 **/
public class ListCryptHandler implements CryptHandler<List> {

    @Override
    public Object encrypt(List param, CryptField cryptField) {
        if (needCrypt(param, cryptField)) {
            return param.stream()
                    .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptField).encrypt(item, cryptField))
                    .collect(Collectors.toList());
        }
        return param;
    }

    @Override
    public Object decrypt(List param, CryptField cryptField) {
        if (needCrypt(param, cryptField)) {
            return param.stream()
                    .map(item -> CryptHandlerFactory.getCryptHandler(item, cryptField).decrypt(item, cryptField))
                    .collect(Collectors.toList());
        }
        return param;
    }

    private boolean needCrypt(List param, CryptField cryptField) {
        return cryptField != null && param != null && param.size() > 0;
    }
}
