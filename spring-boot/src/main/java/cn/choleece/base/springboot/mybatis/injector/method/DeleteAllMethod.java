package cn.choleece.base.springboot.mybatis.injector.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author choleece
 * @Description: 删除所有数据(不带条件, 这里是指一个自定义都方法)
 * @Date 2020-09-13 00:27
 **/
public class DeleteAllMethod extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 执行的sql
        String sql = "delete from " + tableInfo.getTableName();
        // mapper 接口里的方法名
        String method = "deleteAll";
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addDeleteMappedStatement(mapperClass, method, sqlSource);
    }
}
