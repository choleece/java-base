package cn.choleece.base.pattern.builder;

/**
 * 构造者模式
 * @author choleece
 */
public class BuilderTest {
    public static void main(String[] args) {
        Builder builder = new ConcreteBuilder();

        Director director = new Director(builder);

        Product product = director.construct();

        product.show();


        /**
         * 常用的链式调用发
         */
        Product product1 = new Product.ProductBuilder()
                .setPartA("A").setPartB("part b").build();
    }
}
