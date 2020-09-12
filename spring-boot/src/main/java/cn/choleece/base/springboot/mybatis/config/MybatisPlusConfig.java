package cn.choleece.base.springboot.mybatis.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author choleece
 * @Description: mp 配置
 * @Date 2020-09-12 21:55
 **/
@Configuration
@MapperScan("cn.choleece.base.springboot.mybatis.mapper*")
public class MybatisPlusConfig {

    public static ThreadLocal<String> tableNames = new ThreadLocal<>();

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));

        List<ISqlParser> sqlParserList = new LinkedList<>();
        // 动态表
         sqlParserList.add(dynamicTableNameParser());
        // 多租户
        // sqlParserList.add(tenantSqlParser());

        paginationInterceptor.setSqlParserList(sqlParserList);

        // sql 过滤器， 注意事项，如果有过滤器，则parserList的逻辑都不会走
        paginationInterceptor.setSqlParserFilter(metaObject -> {
            // 根据mapper statement去过滤，或者在mapper里的方法上加上注解@SqlParser(filter=true)，就会过滤掉
            // 下面为一种写死的方法，仅仅作为一个例子， 取命名空间下某一个具体的方法不过滤， 实际使用阔以使用@SqlParser
            MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
            if ("cn.choleece.base.springboot.mybatis.mapper.listSysUsers".equals(mappedStatement.getId())) {
                return true;
            }
            return false;
        });

        return paginationInterceptor;
    }

    /**
     * 生成动态表名
     * @return 返回实际等表名
     */
    public DynamicTableNameParser dynamicTableNameParser() {
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        dynamicTableNameParser.setTableNameHandlerMap(new HashMap<String, ITableNameHandler>(2) {{
            put("sys_user", (metaObject, sql, tableName) -> tableNames.get());
        }});

        return dynamicTableNameParser;
    }

    /**
     * 操作后，会在条件中默认带上某一具体字段和值，也就是tenantIdColumn = tenantId， 插入的时候也会把talent_id字段会自动填充上
     * @return
     */
    public TenantSqlParser tenantSqlParser() {
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId(boolean where) {
                // 这里应该从登录session里取出，这里放一个固定的
                return new LongValue(1304803718273851394L);
            }

            @Override
            public String getTenantIdColumn() {
                // 数据库中的字段，根据这个字段，去决定哪个租户
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 如果是sys_role, 就不需要进行多租户判断
                if ("sys_role".equals(tableName)) {
                    return true;
                }
                return false;
            }
        });

        return tenantSqlParser;
    }
}
