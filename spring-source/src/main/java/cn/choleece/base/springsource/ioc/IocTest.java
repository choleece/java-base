package cn.choleece.base.springsource.ioc;

import cn.choleece.base.springsource.ioc.service.IMessageService;
import cn.choleece.base.springsource.util.SpringContextUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author choleece
 * @Description: https://www.javadoop.com/post/spring-ioc
 * @Date 2019-09-19 23:32
 **/
public class IocTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        System.out.println("context 启动成功");

        IMessageService messageService = context.getBean(IMessageService.class);

        IMessageService messageService1 =  (IMessageService) SpringContextUtils.getBean("messageService");

        System.out.println(messageService.getMessage());

        System.out.println(messageService1.getMessage());
    }

}
