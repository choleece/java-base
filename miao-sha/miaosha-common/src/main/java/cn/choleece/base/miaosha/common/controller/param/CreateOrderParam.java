package cn.choleece.base.miaosha.common.controller.param;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-05-03 15:43
 **/
@Data
public class CreateOrderParam {

    /**
     * 购买人ID
     */
    @NotNull
    private Long userId;

    /**
     * 收件人地址id
     */
    private Long deliveryAddrId;

    /**
     * 商品编号
     */
    @NotNull
    private Long goodsId;

    /**
     * 购买个数
     */
    @NotNull
    @Min(value = 1)
    private Integer goodsCount;

    /**
     *
     */
    @NotNull
    private Integer orderChannel;
}
