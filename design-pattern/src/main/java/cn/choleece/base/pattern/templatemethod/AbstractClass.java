package cn.choleece.base.pattern.templatemethod;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-15 13:23
 * 抽象类（Abstract Class）：负责给出一个算法的轮廓和骨架。它由一个模板方法和若干个基本方法构成。这些方法的定义如下。
 *
 * ① 模板方法：定义了算法的骨架，按某种顺序调用其包含的基本方法。
 *
 * ② 基本方法：是整个算法中的一个步骤，包含以下几种类型。
 * 抽象方法：在抽象类中申明，由具体子类实现。
 * 具体方法：在抽象类中已经实现，在具体子类中可以继承或重写它。
 * 钩子方法：在抽象类中已经实现，包括用于判断的逻辑方法和需要子类重写的空方法两种。
 **/
public abstract class AbstractClass {

    /**
    * @Description: 模版方法
    * @methodName:
    * @Param:
    * @return:
    * @Author: choleece
    * @Time: 13:25
    */
    public void templateMethod() {
        concreteMethod();
        abstractMethodOne();
        abstractMethodTwo();
    }

    /**
     * 如果不想此方法被重写，可以在其上边加上final关键字
     */
    public final void concreteMethod() {
        System.out.println("concrete method...");
    }

    /**
     * 抽象方法1
     */
    public abstract void abstractMethodOne();

    /**
     * 抽象方法2
     */
    public abstract void abstractMethodTwo();

}
