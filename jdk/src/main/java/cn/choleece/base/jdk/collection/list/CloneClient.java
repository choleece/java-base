package cn.choleece.base.jdk.collection.list;

import lombok.Data;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-05-08 00:16
 **/
public class CloneClient {

    public static void main(String[] args) throws CloneNotSupportedException {
        A a = new A();
        a.setName("choleece");

        A cloneA = (A) a.clone();
        cloneA.setName("chaoli");

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
