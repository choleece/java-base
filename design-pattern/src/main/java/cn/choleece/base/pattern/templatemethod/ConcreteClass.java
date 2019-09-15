package cn.choleece.base.pattern.templatemethod;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-15 13:25
 **/
public class ConcreteClass extends AbstractClass {

    @Override
    public void abstractMethodOne() {
        System.out.println("抽象方法1的具体实现");
    }

    @Override
    public void abstractMethodTwo() {
        System.out.println("抽象方法2的具体实现");
    }
}
