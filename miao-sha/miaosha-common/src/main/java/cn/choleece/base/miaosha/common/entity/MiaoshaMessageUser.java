package cn.choleece.base.miaosha.common.entity;

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
public class MiaoshaMessageUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userid;

    private Long messageid;

    private Integer goodid;

    private Integer orderid;


}
