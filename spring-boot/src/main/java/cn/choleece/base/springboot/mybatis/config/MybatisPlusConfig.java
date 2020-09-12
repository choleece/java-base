package cn.choleece.base.springboot.mybatis.config;

import com.baomidou.mybatisplus.extension.parsers.DynamicTableNameParser;
import com.baomidou.mybatisplus.extension.parsers.ITableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author choleece
 * @Description: mp 配置
 * @Date 2020-09-12 21:55
 **/
@Configuration
@MapperScan("cn.choleece.base.springboot.mybatis.mapper*")
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));

        paginationInterceptor.setSqlParserList(Collections.singletonList(dynamicTableNameParser()));

        return paginationInterceptor;
    }

    /**
     * 生成动态表名
     * @return 返回实际等表名
     */
    public DynamicTableNameParser dynamicTableNameParser() {
        DynamicTableNameParser dynamicTableNameParser = new DynamicTableNameParser();
        dynamicTableNameParser.setTableNameHandlerMap(new HashMap<String, ITableNameHandler>(2) {{
            put("sys_user", (metaObject, sql, tableName) -> {
                System.out.println(metaObject.getGetterNames());
                System.out.println(metaObject.toString());
                System.out.println(sql);
                System.out.println(tableName);
                // metaObject 可以获取传入参数，这里实现你自己的动态规则
                String year = "";
                int random = new Random().nextInt(10);
                if (random % 2 == 1) {
                    year = "";
                }
                return tableName + year;
            });
        }});

        return dynamicTableNameParser;
    }
}
