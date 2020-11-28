package cn.choleece.base.pattern.responsibility.demo2;

/**
 * @author choleece
 * @Description: 第二种责任链的方式，可中断
 * @Date 2020-09-16 21:46
 **/
public class InterceptorTest {

    /**
     * 当需要在哪里停的时候，直接返回，不调用chain.plugin即可
     * 这里可以控制填充进list里的顺序控制调用的顺序
     * @param args
     */
    public static void main(String[] args) {
        InterceptorChain chain = new InterceptorChain();
        chain.addInterceptor(new InterceptorA());
        chain.addInterceptor(new InterceptorB());
        chain.addInterceptor(new InterceptorC());
        chain.plugin(new Object());

    }
}
