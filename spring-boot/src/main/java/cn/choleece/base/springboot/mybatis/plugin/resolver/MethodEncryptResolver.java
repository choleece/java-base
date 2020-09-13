package cn.choleece.base.springboot.mybatis.plugin.resolver;

/**
 * @author choleece
 * @Description: 加密resolver
 * @Date 2020-09-13 21:06
 **/
public interface MethodEncryptResolver {

    /**
     * 处理加密
     * @param param
     * @return
     */
    Object processEncrypt(Object param);

}
