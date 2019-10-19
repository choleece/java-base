package cn.choleece.base.pattern.abstractfactory;

public class ConcreteProductOne implements AbstractProduct {

    @Override
    public void show() {
        System.out.println("I am product one");
    }
}
