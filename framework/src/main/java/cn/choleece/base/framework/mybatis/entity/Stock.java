package cn.choleece.base.framework.mybatis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-18 23:24
 **/
@Data
@Accessors(chain = true)
public class Stock {

    private Long id;

    private String name;

    private Integer count;

    private Integer sale;

    private Integer version;
}
