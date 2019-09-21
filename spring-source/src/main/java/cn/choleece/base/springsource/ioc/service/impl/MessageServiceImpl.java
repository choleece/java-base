package cn.choleece.base.springsource.ioc.service.impl;

import cn.choleece.base.springsource.ioc.service.IMessageService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-19 23:35
 **/
public class MessageServiceImpl implements IMessageService, BeanFactoryPostProcessor {

    public String getMessage() {
        return "hello world";
    }

    /**
     * 所以如果在bean注册到容器后进行一些操作，可以在这里实现BeanFactoryPostProcessor
     * @param configurableListableBeanFactory
     * @throws BeansException
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("重启初始化后，会执行这个方法");
    }
}
