package cn.choleece.base.pattern.responsibility.demo1;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-16 21:46
 **/
public class InterceptorTest {

    public static void main(String[] args) {
        InterceptorChain chain = new InterceptorChain();
        chain.addInterceptor(new InterceptorA());
        chain.addInterceptor(new InterceptorB());
        chain.addInterceptor(new InterceptorC());
        chain.pluginAll(new Object());
    }

}
