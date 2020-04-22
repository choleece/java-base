package cn.choleece.base.pattern.bridge;

/**
 * @author choleece
 * @Description: 桥接模式
 * @Date 2020-04-22 23:18
 **/
public interface Brand {

    /**
     * 开机
     */
    void open();

    /**
     * 关机
     */
    void close();

    /**
     * 打电话
     */
    void call();
}
