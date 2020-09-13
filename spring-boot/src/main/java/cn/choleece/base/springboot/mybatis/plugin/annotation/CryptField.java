package cn.choleece.base.springboot.mybatis.plugin.annotation;

import cn.choleece.base.springboot.mybatis.plugin.executor.CryptType;

import java.lang.annotation.*;

/**
 * @author choleece
 * @Description: mybatis 数据加解密
 * @Date 2020-09-13 10:46
 **/
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CryptField {

    CryptType value() default CryptType.DEFAULT;

    boolean encrypt() default true;

    boolean decrypt() default true;
}
