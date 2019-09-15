package cn.choleece.base.pattern.decorator;

/**
 * 装饰器模式测试
 * @author choleece
 */
public class DecoratorTest {

    public static void main(String[] args) {
        Component component = new ConcreteComponent();

        component.operation();

        System.out.println("------------");

        Decorator decorator = new ConcreteDecorator(component);

        decorator.operation();
    }

}
