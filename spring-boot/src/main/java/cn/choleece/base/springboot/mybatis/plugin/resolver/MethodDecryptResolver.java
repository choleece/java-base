package cn.choleece.base.springboot.mybatis.plugin.resolver;

/**
 * @author choleece
 * @Description: 处理解密
 * @Date 2020-09-13 21:07
 **/
public interface MethodDecryptResolver {

    /**
     * 处理解密
     * @param param
     * @return
     */
    Object processDecrypt(Object param);

}
