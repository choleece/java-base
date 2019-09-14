package cn.choleece.base.pattern.builder;

public abstract class Builder {

    protected Product product = new Product();

    abstract void setPartA();

    abstract void setPartB();

    abstract void setPartC();

    public Product getProduct() {
        return product;
    }
}
