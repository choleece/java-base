package cn.choleece.base.pattern.builder;

/**
 * 构造者模式，还有一种builder模式，可以让外部灵活的实例化对象
 * @author choleece
 */
public class Product {

    private String partA;

    private String partB;

    private String partC;

    public Product() {
    }

    public void setPartA(String partA) {
        this.partA = partA;
    }

    public void setPartB(String partB) {
        this.partB = partB;
    }

    public void setPartC(String partC) {
        this.partC = partC;
    }

    public void show() {
        System.out.println("展示产品全貌");
    }

    public Product(ProductBuilder builder) {
        this.partA = builder.getPartA();
        this.partB = builder.getPartB();
        this.partC = builder.getPartC();
    }

    public static class ProductBuilder {
        private String partA;

        private String partB;

        private String partC;

        protected String getPartA() {
            return partA;
        }

        public ProductBuilder setPartA(String partA) {
            this.partA = partA;
            return this;
        }

        protected String getPartB() {
            return partB;
        }

        public ProductBuilder setPartB(String partB) {
            this.partB = partB;
            return this;
        }

        protected String getPartC() {
            return partC;
        }

        public ProductBuilder setPartC(String partC) {
            this.partC = partC;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
