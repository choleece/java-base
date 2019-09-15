package cn.choleece.base.pattern.adapter;

/**
 * @Description: 装饰器模式测试 (有两种写法)
 * @author choleece
 * @Date 2019-09-15 13:05
 **/
public class AdapterTest {

    public static void main(String[] args) {
        Target target = new Adapter();

        target.request();

        SecondAdapter adapter = new SecondAdapter(new Adaptee());

        adapter.request();
    }
}
