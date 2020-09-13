package cn.choleece.base.springboot.mybatis.plugin.resolver;

import cn.choleece.base.springboot.mybatis.plugin.annotation.CryptField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author choleece
 * @Description: 方法加密注解了的参数
 * @Date 2020-09-13 21:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodAnnotationEncryptParameter {

    private String paramName;
    private CryptField cryptField;
    private Class cls;

}
