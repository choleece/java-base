package cn.choleece.base.pattern.factorymethod;

public class ConcreteFactoryOne implements AbstractFactory {

    public AbstractProduct createProduct() {
        return new ProductOne();
    }
}
