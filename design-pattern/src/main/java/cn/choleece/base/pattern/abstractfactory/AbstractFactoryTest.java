package cn.choleece.base.pattern.abstractfactory;

/**
 * 抽象工厂
 * @author choleece
 */
public class AbstractFactoryTest {

    public static void main(String[] args) {
        AbstractFactory factory = new ConcreteFactoryOne();

        factory.createProductOne().show();
        factory.createProductTwo().show();
    }
}
