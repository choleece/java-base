package cn.choleece.base.pattern.abstractfactory;

/**
 * @author choleece
 */
public class ConcreteFactoryOne implements AbstractFactory {

    @Override
    public ConcreteProductOne createProductOne() {
        return new ConcreteProductOne();
    }

    @Override
    public ConcreteProductTwo createProductTwo() {
        return new ConcreteProductTwo();
    }
}
