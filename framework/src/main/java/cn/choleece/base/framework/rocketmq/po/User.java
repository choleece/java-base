package cn.choleece.base.framework.rocketmq.po;

import lombok.Builder;
import lombok.Data;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-12-28 22:35
 */
@Data
@Builder
public class User {

    private String name;

    private String email;
}
