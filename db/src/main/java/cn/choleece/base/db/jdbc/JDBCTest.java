package cn.choleece.base.db.jdbc;

import java.sql.*;

/**
 * @author choleece
 * @Description: JDBC 通过JDBC 引出链接池 ORM 框架
 * @Date 2019-11-06 21:09
 **/
public class JDBCTest {

    static final String BASE_URL = "jdbc:mysql://www.brotech.club:3506/test_db";

    static final String USER = "cyber";

    static final String PASSWORD = "123456";

    static final String CLASS_DRIVER = "com.mysql.jdbc.Driver";

    public static void main(String[] args) {

        /**
         * mysql 链接
         */
        Connection connection = null;

        /**
         * mysql statement(可以简单理解成一条sql的执行句柄)
         * 参考：https://www.cnblogs.com/java-memory/p/9182203.html
         * 还有 PreparedStatement（预编译），也需要了解下
         *
         * JDBC 还有考虑事物，默认自动提交 可以通过setAutoCommit去设置
         */
        Statement statement = null;

        try {

            /**
             * 加载驱动 这里用Class.forName,可以看的Driver实例化的时候，其实底层有static方法，实现初始化
             * 可以看到Driver里有个静态代码块，点开com.mysql.jdbc.Driver 类，DriverManager会先加载一个driver到DriverManager里，driver 容器底层是一个 COW数组， CopyOnWriteArrayList<DriverInfo> registeredDrivers
             * SPI: Service Provider Interface 可以考虑是否可以SPI的形式
             * class load & Class.forName 区别
             */
//            static {
//                try {
//                    DriverManager.registerDriver(new Driver());
//                } catch (SQLException var1) {
//                    throw new RuntimeException("Can't register driver!");
//                }
//            }
            Class.forName(CLASS_DRIVER);

            /**
             * 建立链接
             */
            System.out.println("--建立链接--");
            connection = DriverManager.getConnection(BASE_URL, USER, PASSWORD);

            System.out.println("--链接建立成功--");

            statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("select * from city");

            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }

            // 获取数据库元数据，表名，字段名啥的
            DatabaseMetaData metaData = connection.getMetaData();

            rs.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
