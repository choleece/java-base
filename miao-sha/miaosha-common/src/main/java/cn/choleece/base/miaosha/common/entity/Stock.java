package cn.choleece.base.miaosha.common.entity;

import java.math.BigDecimal;
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
public class Stock extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品标题
     */
    private int count;

    /**
     * 商品的图片
     */
    private int sale;

    /**
     * 商品的详情介绍
     */
    private int version;
}
