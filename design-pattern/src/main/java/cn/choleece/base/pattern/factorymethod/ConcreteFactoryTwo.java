package cn.choleece.base.pattern.factorymethod;

public class ConcreteFactoryTwo implements AbstractFactory {

    @Override
    public AbstractProduct createProduct() {
        return new ProductTwo();
    }
}
