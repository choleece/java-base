package cn.choleece.base.pattern.decorator;

/**
 * 具体构件（Concrete Component）角色：实现抽象构件，通过装饰角色为其添加一些职责。
 * @author choleece
 */
public class ConcreteComponent implements Component {

    public ConcreteComponent() {
        System.out.println("创建具体构建");
    }

    @Override
    public void operation() {
        System.out.println("具体构建的具体方法");
    }
}
