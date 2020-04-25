package cn.choleece.base.miaosha.common.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class MiaoshaMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分布式id
     */
    private Long messageid;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 1 有效 2 失效 
     */
    private Integer status;

    /**
     * 结束时间
     */
    private LocalDateTime overTime;

    /**
     * 0 秒杀消息 1 购买消息 2 推送消息
     */
    private Integer messageType;

    /**
     * 发送类型 0 app 1 pc 2 ios
     */
    private Integer sendType;

    /**
     * 商品名称
     */
    private String goodName;

    /**
     * 商品价格
     */
    private BigDecimal price;


}
