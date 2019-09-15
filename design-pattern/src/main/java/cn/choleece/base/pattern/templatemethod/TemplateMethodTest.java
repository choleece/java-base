package cn.choleece.base.pattern.templatemethod;

/**
 * @author choleece
 * @Description: 模版方法模式测试 封装不变的，变化的部分交给子类去实现或重写
 * @Date 2019-09-15 13:26
 **/
public class TemplateMethodTest {

    public static void main(String[] args) {
        AbstractClass clazz = new ConcreteClass();

        clazz.templateMethod();
    }

}
