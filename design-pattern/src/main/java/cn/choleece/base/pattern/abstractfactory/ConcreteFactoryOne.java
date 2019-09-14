package cn.choleece.base.pattern.abstractfactory;

/**
 * @author choleece
 */
public class ConcreteFactoryOne implements AbstractFactory {

    public ConcreteProductOne createProductOne() {
        return new ConcreteProductOne();
    }

    public ConcreteProductTwo createProductTwo() {
        return new ConcreteProductTwo();
    }
}
