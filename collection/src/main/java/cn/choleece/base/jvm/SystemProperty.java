package cn.choleece.base.jvm;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author choleece
 * @Description: Java 系统属性
 * @Date 2019-11-19 23:17
 *
 * 参考：https://www.cnblogs.com/EasonJim/p/7610910.html
 **/
public class SystemProperty {

    public static void main(String[] args) {
        Map<String,String> map = System.getenv();
        Set<Map.Entry<String,String>> entries = map.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        System.out.println(map.get("ENV"));

        Properties properties = System.getProperties();
        Set<Map.Entry<Object, Object>> set = properties.entrySet();
        for (Map.Entry<Object, Object> objectObjectEntry : set) {
            System.out.println(objectObjectEntry.getKey() + ":" + objectObjectEntry.getValue());
        }

        System.out.println(properties.getProperty("env"));
    }
}
