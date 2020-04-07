package cn.choleece.base.pattern.adapter.classadapter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-07 22:31
 **/
public class VoltageAdapter extends Voltage220V implements IVoltage5V {

    @Override
    public int outPut5V() {
        int srcV = super.outPut220V();

        int dstV = srcV / 44;

        System.out.println("适配器适配完成后的电压: " + dstV);
        return dstV;
    }
}
