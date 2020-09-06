package cn.choleece.base.miaosha.common.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import cn.choleece.base.miaosha.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * stock id
     */
    private String sId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品数量
     */
    private String createTime;
}
