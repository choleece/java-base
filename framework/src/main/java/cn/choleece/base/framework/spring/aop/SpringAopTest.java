package cn.choleece.base.framework.spring.aop;

import cn.choleece.base.framework.spring.aop.service.IOrderService;
import cn.choleece.base.framework.spring.aop.service.IUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-23 22:48
 **/
public class SpringAopTest {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("aop-beans.xml");

        IUserService userService = context.getBean(IUserService.class);
        IOrderService orderService = context.getBean(IOrderService.class);

        userService.createUser("Tom", "Cruise", 55);
        userService.queryUser();

        orderService.createOrder("Leo", "随便买点什么");
        orderService.queryOrder("Leo");
    }

}
