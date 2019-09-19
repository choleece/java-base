package cn.choleece.base.springsource.ioc;

import cn.choleece.base.springsource.ioc.service.IMessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author choleece
 * @Description: https://www.javadoop.com/post/spring-ioc
 * @Date 2019-09-19 23:32
 **/
public class IocTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");

        System.out.println("context 启动成功");

        IMessageService messageService = context.getBean(IMessageService.class);

        System.out.println(messageService.getMessage());
    }

}
