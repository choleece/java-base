package cn.choleece.base.pattern.decorator;

/**
 * 具体装饰（ConcreteDecorator）角色：实现抽象装饰的相关方法，并给具体构件对象添加附加的责任。
 * @author choleece
 */
public class ConcreteDecorator extends Decorator {

    public ConcreteDecorator(Component component) {
        super(component);
    }

    @Override
    public void operation() {
        super.operation();
        addFunction();
    }

    /**
     * 增加额外的功能
     */
    public void addFunction() {
        System.out.println("增加额外的功能以供使用");
    }
}
