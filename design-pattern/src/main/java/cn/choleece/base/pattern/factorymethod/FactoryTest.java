package cn.choleece.base.pattern.factorymethod;

/**
 * 找到对应的工厂，就可以生产对应的实例
 * @author choleece
 */
public class FactoryTest {

    public static void main(String[] args) {
        AbstractFactory factory = new ConcreteFactoryOne();

        factory.createProduct().show();

        factory = new ConcreteFactoryTwo();

        factory.createProduct().show();
    }

}
