package cn.choleece.base.framework.mybatis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-20 22:36
 **/
@Data
@Accessors(chain = true)
public class SysUser {

    private Long id;

    private String name;
}
