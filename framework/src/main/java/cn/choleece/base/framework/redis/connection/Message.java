package cn.choleece.base.framework.redis.connection;

import java.io.Serializable;

/**
 * @description: 消息载体
 * @author: sf
 * @time: 2019-10-14 18:04
 */
public interface Message extends Serializable {

    byte[] getBody();

    byte[] getChannel();
}
