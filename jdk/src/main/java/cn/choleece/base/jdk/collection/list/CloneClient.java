package cn.choleece.base.jdk.collection.list;

import lombok.Data;

/**
 * @author choleece
 * @Description: 浅克隆只能拷贝基本类型，其引用类型还是原来的引用地址，改变副本，原来的对象也会跟着改变
 * 深克隆会生成两个不同的对象
 * @Date 2020-05-08 00:16
 **/
public class CloneClient {

    public static void main(String[] args) throws CloneNotSupportedException {
        A a = new A();
        a.setName("choleece");
        a.setA(new A());
        a.getA().setName("heihei");

        A cloneA = (A) a.clone();
        cloneA.setName("chaoli");
        cloneA.getA().setName("haha");

        System.out.println(a.getA() == cloneA.getA());

        System.out.println("original a " + a.toString());
        System.out.println("clone a " + cloneA.toString());

        B b = new B();
        b.setSex("F");

        System.out.println("sex " + b.toString());
    }
}

@Data
class A implements Cloneable {
    private String name;

    private A a;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

@Data
class B extends A implements Cloneable {
    private String sex;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
