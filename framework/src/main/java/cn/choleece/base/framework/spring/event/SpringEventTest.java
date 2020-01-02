package cn.choleece.base.framework.spring.event;

import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description: Spring 事件处理
 * @author: choleece
 * @time: 2019-11-20 09:24
 */
public class SpringEventTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new ClassPathXmlApplicationContext("spring-event-beans.xml");
    }

    public static class SpringEventBean {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void printMessage() {
            System.out.println("message: " + this.message);
        }
    }

    public static class ContextRefreshEventBean implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
            System.out.println("contextRefreshedEvent...");
        }
    }

    public static class ContextStartEventBean implements ApplicationListener<ContextStartedEvent> {
        @Override
        public void onApplicationEvent(ContextStartedEvent contextStartedEvent) {
            System.out.println("ContextStartedEvent...");
        }
    }

    public static class ContextStopEventBean implements ApplicationListener<ContextStoppedEvent> {
        @Override
        public void onApplicationEvent(ContextStoppedEvent contextStoppedEvent) {
            System.out.println("ContextStoppedEvent...");
        }
    }

    public static class ContextClosedEventBean implements ApplicationListener<ContextClosedEvent> {
        @Override
        public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
            System.out.println("contextClosedEvent...");
        }
    }
}
