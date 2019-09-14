package cn.choleece.base.pattern.builder;

public class ConcreteBuilder extends Builder {

    @Override
    void setPartA() {
        product.setPartA("我要设置part a");
    }

    @Override
    void setPartB() {
        product.setPartB("我要设置part b");
    }

    @Override
    void setPartC() {
        System.out.println("我要设置part c");
        product.setPartC("我要设置part c");
    }
}
