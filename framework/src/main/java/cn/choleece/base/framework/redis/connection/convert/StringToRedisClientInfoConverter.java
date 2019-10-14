package cn.choleece.base.framework.redis.connection.convert;

import cn.choleece.base.framework.redis.core.types.RedisClientInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:30
 **/
public class StringToRedisClientInfoConverter implements Converter<String[], List<RedisClientInfo>> {
    public StringToRedisClientInfoConverter() {
    }

    @Override
    public List<RedisClientInfo> convert(String[] lines) {
        List<RedisClientInfo> clientInfoList = new ArrayList(lines.length);
        String[] var3 = lines;
        int var4 = lines.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String line = var3[var5];
            clientInfoList.add(RedisClientInfo.RedisClientInfoBuilder.fromString(line));
        }

        return clientInfoList;
    }
}
