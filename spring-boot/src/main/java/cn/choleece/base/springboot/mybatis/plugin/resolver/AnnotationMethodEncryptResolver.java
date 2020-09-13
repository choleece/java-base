package cn.choleece.base.springboot.mybatis.plugin.resolver;

import cn.choleece.base.springboot.mybatis.plugin.handler.CryptHandlerFactory;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author choleece
 * @Description: 有注解的方法加密处理者
 * @Date 2020-09-13 22:25
 **/
@AllArgsConstructor
public class AnnotationMethodEncryptResolver  implements MethodEncryptResolver {

    private List<MethodAnnotationEncryptParameter> methodAnnotationEncryptParameterList;

    @Override
    public Object processEncrypt(Object param) {
        Map map = (Map) param;
        methodAnnotationEncryptParameterList.forEach(item ->
                map.computeIfPresent(item.getParamName(), (key, oldValue) ->
                        CryptHandlerFactory.getCryptHandler(oldValue, item.getCryptField()).encrypt(oldValue, item.getCryptField())
                )
        );
        return param;
    }

}
