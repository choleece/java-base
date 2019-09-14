package cn.choleece.base.pattern.factorymethod;

public class ConcreteFactoryTwo implements AbstractFactory {

    public AbstractProduct createProduct() {
        return new ProductTwo();
    }
}
