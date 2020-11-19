package cn.choleece.base.framework.spring.annotation;

import org.springframework.context.annotation.Import;

/**
 * @author choleece
 * @Description: 第一种形式，直接写入要注入的类的类名数组
 * @Date 2020-11-19 22:03
 **/
@Import({BaseClassOne.class, BaseClassTwo.class})
public class ImportTypeOne {
}
