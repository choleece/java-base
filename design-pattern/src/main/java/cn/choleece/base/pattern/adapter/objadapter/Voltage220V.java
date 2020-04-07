package cn.choleece.base.pattern.adapter.objadapter;

/**
 * @author choleece
 * @Description: 被装饰者
 * @Date 2020-04-07 22:30
 **/
public class Voltage220V {

    public int outPut220V() {
        System.out.println("输出220V电压");
        return 220;
    }
}
