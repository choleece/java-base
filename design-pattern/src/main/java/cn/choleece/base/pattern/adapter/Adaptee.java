package cn.choleece.base.pattern.adapter;

/**
 * @Description: 适配者（Adaptee）类：它是被访问和适配的现存组件库中的组件接口。
 * @author choleece
 * @Date 2019-09-15 13:00
 **/
public class Adaptee {

    public void specificRequest() {
        System.out.println("可以是改变了的方法");
    }

}
