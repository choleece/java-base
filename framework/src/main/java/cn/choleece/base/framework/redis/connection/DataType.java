package cn.choleece.base.framework.redis.connection;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Redis 数据类型
 * @author: sf
 * @time: 2019-10-14 15:26
 */
public enum DataType {

    NONE("none"),
    STRING("string"),
    LIST("list"),
    SET("set"),
    ZSET("zset"),
    HASH("hash");

    private static final Map<String, DataType> codeLookup = new ConcurrentHashMap(6);
    private final String code;

    DataType(String name) {
        this.code = name;
    }

    public String code() {
        return this.code;
    }

    public static DataType fromCode(String code) {
        DataType data = (DataType)codeLookup.get(code);
        if (data == null) {
            throw new IllegalArgumentException("unknown data type code");
        } else {
            return data;
        }
    }

    static {
        Iterator var0 = EnumSet.allOf(DataType.class).iterator();

        while(var0.hasNext()) {
            DataType type = (DataType)var0.next();
            codeLookup.put(type.code, type);
        }

    }
}
