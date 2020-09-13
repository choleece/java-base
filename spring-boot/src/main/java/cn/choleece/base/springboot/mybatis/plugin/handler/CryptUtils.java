package cn.choleece.base.springboot.mybatis.plugin.handler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author choleece
 * @Description: 加解密工具
 * @Date 2020-09-13 14:30
 **/
public class CryptUtils {

    private static final Set<Class> IGNORE_CLASS = new HashSet<>();

    static {
        IGNORE_CLASS.add(Byte.class);
        IGNORE_CLASS.add(Short.class);
        IGNORE_CLASS.add(Integer.class);
        IGNORE_CLASS.add(Long.class);
        IGNORE_CLASS.add(Float.class);
        IGNORE_CLASS.add(Double.class);
        IGNORE_CLASS.add(Boolean.class);
        IGNORE_CLASS.add(Character.class);
    }

    public static boolean inIgnoreClass(Class cls) {
        return IGNORE_CLASS.contains(cls);
    }

}
