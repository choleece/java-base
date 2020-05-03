package cn.choleece.base.miaosha.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-25 21:42
 **/
public class BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = String.valueOf(id);
    }
}
