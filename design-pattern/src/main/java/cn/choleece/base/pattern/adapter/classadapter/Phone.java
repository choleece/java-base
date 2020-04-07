package cn.choleece.base.pattern.adapter.classadapter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-07 22:33
 **/
public class Phone {

    public void charging(IVoltage5V voltage5V) {
        int adapterVol = voltage5V.outPut5V();

        if (adapterVol == 5) {
            System.out.println("电压5V,正在充电");
        } else {
            System.out.println("电压不匹配...");
        }
    }
}
