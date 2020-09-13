package cn.choleece.base.springboot.mybatis.plugin.resolver;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;
import cn.choleece.base.springboot.mybatis.plugin.handler.CryptHandlerFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-13 21:52
 **/
@Getter
@AllArgsConstructor
public class SimpleMethodDecryptResolver  implements MethodDecryptResolver {

    private CryptField cryptField;

    @Override
    public Object processDecrypt(Object param) {
        return CryptHandlerFactory.getCryptHandler(param, cryptField).decrypt(param, cryptField);
    }

}
