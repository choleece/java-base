package cn.choleece.base.springboot.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author choleece
 * @Description: 用户表
 * @Date 2020-09-12 21:13
 **/
@Data
@Accessors(chain = true)
public class SysUser extends BaseEntity {

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    private String phone;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 更新时间 随数据库系统更新
     */
    private String mgtModified;

    /**
     * 创建人名字，插入时自动写入 这里不论是否有值，都会按照自动的内容进行填充
     */
    @TableField(fill = FieldFill.INSERT)
    private String createName;

    /**
     * 代码自动更新
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    /**
     * 逻辑删除 注解里该字段，调用deleteById后，实际执行的是update 操作， 这里的del value有全局配置
     * table logic 里有两个参数 value(代表未删除的状态, 比如0), del val(代表删除的状态, 比如1)
     */
    @TableLogic
    private Integer deleted;
}
