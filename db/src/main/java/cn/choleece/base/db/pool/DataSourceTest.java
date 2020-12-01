package cn.choleece.base.db.pool;

import lombok.Data;

import java.util.function.Function;

/**
 * @author choleece
 * @Description: 数据库链接池 HikariCP
 * @Date 2019-11-06 21:55
 * <p>
 * 至于HikariCP 为什么这么快，可以参考:https://www.jianshu.com/p/dd0c2b0ed202?utm_campaign
 * <p>
 * HikariCP 简单描述:https://www.jianshu.com/p/d64ea54c8e0b
 **/
public class DataSourceTest {

    @Data
    public static class Book {
        private String name;

        private Integer price;

        public Book(String name, Integer price) {
            this.name = name;
            this.price = price;
        }
    }

    public static void main(String[] args) {
        Function<String, String> function = (t) -> t + "1";

        System.out.println(function.apply("10"));

    }
}
