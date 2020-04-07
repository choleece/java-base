package cn.choleece.base.pattern.adapter.objadapter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-07 22:31
 **/
public class VoltageAdapter implements IVoltage5V {

    private Voltage220V voltage220V;

    public VoltageAdapter(Voltage220V voltage220V) {
        this.voltage220V = voltage220V;
    }

    @Override
    public int outPut5V() {
        int srcV = voltage220V.outPut220V();

        int dstV = srcV / 44;

        System.out.println("适配器适配完成后的电压: " + dstV);
        return dstV;
    }
}
