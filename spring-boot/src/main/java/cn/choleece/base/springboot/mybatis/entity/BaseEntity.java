package cn.choleece.base.springboot.mybatis.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import static com.baomidou.mybatisplus.annotation.IdType.ASSIGN_ID;

/**
 * @author choleece
 * @Description: 主键ID
 * @Date 2020-09-12 21:13
 **/
@Data
@Accessors(chain = true)
public abstract class BaseEntity {
    @TableId(type = ASSIGN_ID)
    private Long id;
}
