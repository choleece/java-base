package cn.choleece.base.springboot.mybatis.plugin.resolver;

/**
 * @author choleece
 * @Description: 空的加密resolver
 * @Date 2020-09-13 21:52
 **/
public class EmptyMethodEncryptResolver implements MethodEncryptResolver {

    @Override
    public Object processEncrypt(Object param) {
        System.out.println("empty");
        return param;
    }

}