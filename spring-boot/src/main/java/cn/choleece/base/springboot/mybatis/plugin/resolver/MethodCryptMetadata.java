package cn.choleece.base.springboot.mybatis.plugin.resolver;

import lombok.Data;

/**
 * @author choleece
 * @Description: 方法加解密 元数据
 * @Date 2020-09-13 21:05
 **/
@Data
public class MethodCryptMetadata {

    /**
     * 方法加密处理和
     */
    public MethodEncryptResolver methodEncryptResolver;

    /**
     * 方法解密处理者
     */
    public MethodDecryptResolver methodDecryptResolver;

    public Object encrypt(Object object) {
        if (object == null) {
            return null;
        }
        return methodEncryptResolver.processEncrypt(object);
    }

    public Object decrypt(Object object) {
        if (object == null) {
            return null;
        }
        return methodDecryptResolver.processDecrypt(object);
    }

}
