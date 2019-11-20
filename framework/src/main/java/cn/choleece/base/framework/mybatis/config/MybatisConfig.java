package cn.choleece.base.framework.mybatis.config;

import cn.choleece.base.db.pool.hikari.HikariConfig;
import cn.choleece.base.db.pool.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-20 22:34
 **/
@Configuration
public class MybatisConfig {

    static final String BASE_URL = "jdbc:mysql://www.brotech.club:3506/test_db";

    static final String USER = "cyber";

    static final String PASSWORD = "123456";

    static final String CLASS_DRIVER = "com.mysql.jdbc.Driver";

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("cn.choleece.base.framework.mybatis.mapper");
        return mapperScannerConfigurer;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    public DataSource dataSource() {

        HikariConfig jdbcConfig = new HikariConfig();
        jdbcConfig.setPoolName(getClass().getName());
        jdbcConfig.setDriverClassName(CLASS_DRIVER);
        jdbcConfig.setJdbcUrl(BASE_URL);
        jdbcConfig.setUsername(USER);
        jdbcConfig.setPassword(PASSWORD);
        jdbcConfig.setMaximumPoolSize(20);
        jdbcConfig.setMaxLifetime(30000);
        jdbcConfig.setConnectionTimeout(3000);
        jdbcConfig.setIdleTimeout(3000);

        return new HikariDataSource(jdbcConfig);
    }
}
