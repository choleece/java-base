package cn.choleece.base.framework.spring.ioc;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-11-19 14:22
 */
public class HelloWorldBean {

    private String name;

    public void sayHello() {
        System.out.println("hello, " + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Hello {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void sayHello() {
            System.out.println("inner static class, hello " + name);
        }
    }

    public void init() {
        System.out.println("初始化完成后...");
    }

    public void destroy() throws Exception {
        System.out.println("bean销毁后...");
    }
}