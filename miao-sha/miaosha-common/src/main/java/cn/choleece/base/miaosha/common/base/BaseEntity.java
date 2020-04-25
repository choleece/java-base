package cn.choleece.base.miaosha.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-25 21:42
 **/
@Data
public class BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

}
