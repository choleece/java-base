package cn.choleece.base.framework.spring.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author choleece
 * @Description: 第二种形式，以实现ImportSelector的形式来进行注入，通过这种方法，可以实现动态注入
 * @Date 2020-11-19 22:03
 **/
public class ImportTypeTwo implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 这里可以返回空数组，但是不能返回NULL，否则会NPE
//        return new String[0];

        // 这里返回类的全限定名的数组，所以这里可以是动态的
        return new String[] {"cn.choleece.base.framework.spring.annotation.BaseClassOne"};
    }
}
