package cn.choleece.base.miaosha.common.constant;

/**
 * @author choleece
 * @Description: redis key
 * @Date 2020-09-06 16:21
 **/
public class RedisKeysConstant {

    /**
     * 库存
     */
    public final static String STOCK_COUNT = "stock:%s:count";

    /**
     * 已售数量
     */
    public final static String STOCK_SALE = "stock:%s:sale";

    /**
     * 版本
     */
    public final static String STOCK_VERSION = "stock:%s:version";

}
