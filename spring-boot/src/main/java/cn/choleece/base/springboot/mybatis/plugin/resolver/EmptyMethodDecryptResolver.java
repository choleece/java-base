package cn.choleece.base.springboot.mybatis.plugin.resolver;

/**
 * @author choleece
 * @Description: 空的解密resolver
 * @Date 2020-09-13 21:51
 **/
public class EmptyMethodDecryptResolver implements MethodDecryptResolver {

    @Override
    public Object processDecrypt(Object param) {
        System.out.println("empty");
        return param;
    }

}
