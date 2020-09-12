package cn.choleece.base.springboot.mybatis.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author choleece
 * @Description: 自定义自动填充值
 * @Date 2020-09-12 23:10
 **/
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        // 获取当前登录用户， 比如通过session等信息
        // SysUser user = (SysUser)SecurityUtils.getSubject().getPrincipal();

        // 这里一个小优化，判断有此字段的时候才去执行
        if (metaObject.hasSetter("createName")) {
            System.out.println("插入是否自动填充");
            // 插入时自动填充, 这里的fieldName为Java对应的字段名称，非数据库字段名称
            // 这样当有值的时候，就不进行填充，只有当值为空时，才进行填充
            if (getFieldValByName("createName", metaObject) == null) {
                setFieldValByName("createName", "测试自动填充", metaObject);
            }

            setUpdateTimeChanged(metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("更新是否自动填充");
        // 程序更新自动时间
        setUpdateTimeChanged(metaObject);
    }

    public void setUpdateTimeChanged(MetaObject metaObject) {
        setFieldValByName("updateTime", DateUtil.now(), metaObject);
    }
}
