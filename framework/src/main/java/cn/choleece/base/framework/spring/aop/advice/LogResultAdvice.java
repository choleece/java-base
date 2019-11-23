package cn.choleece.base.framework.spring.aop.advice;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-23 22:42
 **/
public class LogResultAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
            throws Throwable {
        System.out.println(method.getName() + "方法返回：" + returnValue);
    }
}
