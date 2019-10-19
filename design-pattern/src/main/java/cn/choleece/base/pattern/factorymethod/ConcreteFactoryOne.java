package cn.choleece.base.pattern.factorymethod;

public class ConcreteFactoryOne implements AbstractFactory {

    @Override
    public AbstractProduct createProduct() {
        return new ProductOne();
    }
}
