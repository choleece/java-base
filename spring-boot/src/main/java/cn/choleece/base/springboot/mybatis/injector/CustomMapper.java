package cn.choleece.base.springboot.mybatis.injector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author choleece
 * @Description: 自定义mapper
 * @Date 2020-09-13 00:35
 **/
public interface CustomMapper<T> extends BaseMapper<T> {

    /**
     * 删除所有
     * @return 返回变更的行数
     */
    int deleteAll();

    /**
     * 批量插入数据
     * @param list
     * @return
     */
    int insertBatchSomeColumn(List<T> list);

}
