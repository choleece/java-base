package cn.choleece.base.framework.mybatis;

import cn.choleece.base.framework.mybatis.mapper.SysUserMapper;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @author choleece
 * @Description: mybatis test
 * @Date 2020-09-17 00:02
 **/
public class MybatisTest {

    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setCacheEnabled(true);
        configuration.addMapper(SysUserMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        SysUserMapper cityMapper = sqlSessionFactory.openSession().getMapper(SysUserMapper.class);
        System.out.println(cityMapper.listUsers());
        System.out.println(cityMapper.listUsers());
        System.out.println(cityMapper.listUsers());
    }

    private static DataSource createDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/miaosha");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");

        return dataSource;
    }
}
