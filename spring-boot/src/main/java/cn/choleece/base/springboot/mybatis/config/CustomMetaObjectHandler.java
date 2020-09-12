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
        System.out.println("插入是否自动填充");
        // 插入时自动填充
        setFieldValByName("create_name", "测试自动填充", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("更新是否自动填充");
        // 程序更新自动时间
        setFieldValByName("update_time", DateUtil.now(), metaObject);
    }
}
