package cn.choleece.base.pattern.builder;

public class Director {

    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    public Product construct() {
        builder.setPartA();
        builder.setPartB();
        builder.setPartC();

        return builder.getProduct();
    }
}
