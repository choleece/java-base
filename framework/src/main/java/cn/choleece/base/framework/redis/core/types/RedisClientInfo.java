package cn.choleece.base.framework.redis.core.types;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:33
 **/
public class RedisClientInfo {

    private final Properties clientProperties;

    public RedisClientInfo(Properties properties) {
        Assert.notNull(properties, "Cannot initialize client information for given 'null' properties.");
        this.clientProperties = new Properties();
        this.clientProperties.putAll(properties);
    }

    public String getAddressPort() {
        return this.get(RedisClientInfo.INFO.ADDRESS_PORT);
    }

    public String getFileDescriptor() {
        return this.get(RedisClientInfo.INFO.FILE_DESCRIPTOR);
    }

    public String getName() {
        return this.get(RedisClientInfo.INFO.CONNECTION_NAME);
    }

    public Long getAge() {
        return this.getLongValueOf(RedisClientInfo.INFO.CONNECTION_AGE);
    }

    public Long getIdle() {
        return this.getLongValueOf(RedisClientInfo.INFO.CONNECTION_IDLE);
    }

    public String getFlags() {
        return this.get(RedisClientInfo.INFO.FLAGS);
    }

    public Long getDatabaseId() {
        return this.getLongValueOf(RedisClientInfo.INFO.DATABSE_ID);
    }

    public Long getChannelSubscribtions() {
        return this.getLongValueOf(RedisClientInfo.INFO.CHANNEL_SUBSCRIBTIONS);
    }

    public Long getPatternSubscrbtions() {
        return this.getLongValueOf(RedisClientInfo.INFO.PATTERN_SUBSCRIBTIONS);
    }

    public Long getMultiCommandContext() {
        return this.getLongValueOf(RedisClientInfo.INFO.MULIT_COMMAND_CONTEXT);
    }

    public Long getBufferLength() {
        return this.getLongValueOf(RedisClientInfo.INFO.BUFFER_LENGTH);
    }

    public Long getBufferFreeSpace() {
        return this.getLongValueOf(RedisClientInfo.INFO.BUFFER_FREE_SPACE);
    }

    public Long getOutputBufferLength() {
        return this.getLongValueOf(RedisClientInfo.INFO.OUTPUT_BUFFER_LENGTH);
    }

    public Long getOutputListLength() {
        return this.getLongValueOf(RedisClientInfo.INFO.OUTPUT_LIST_LENGTH);
    }

    public Long getOutputBufferMemoryUsage() {
        return this.getLongValueOf(RedisClientInfo.INFO.OUTPUT_BUFFER_MEMORY_USAGE);
    }

    public String getEvents() {
        return this.get(RedisClientInfo.INFO.EVENTS);
    }

    public String getLastCommand() {
        return this.get(RedisClientInfo.INFO.LAST_COMMAND);
    }

    public String get(RedisClientInfo.INFO info) {
        Assert.notNull(info, "Cannot retrieve client information for 'null'.");
        return this.clientProperties.getProperty(info.key);
    }

    @Nullable
    public String get(String key) {
        Assert.hasText(key, "Cannot get client information for 'empty' / 'null' key.");
        return this.clientProperties.getProperty(key);
    }

    private Long getLongValueOf(RedisClientInfo.INFO info) {
        String value = this.get(info);
        return value == null ? null : Long.valueOf(value);
    }

    @Override
    public String toString() {
        return this.clientProperties.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RedisClientInfo)) {
            return false;
        } else {
            RedisClientInfo other = (RedisClientInfo)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$clientProperties = this.clientProperties;
                Object other$clientProperties = other.clientProperties;
                if (this$clientProperties == null) {
                    if (other$clientProperties != null) {
                        return false;
                    }
                } else if (!this$clientProperties.equals(other$clientProperties)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RedisClientInfo;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $clientProperties = this.clientProperties;
        result = result * 59 + ($clientProperties == null ? 43 : $clientProperties.hashCode());
        return result;
    }

    public static class RedisClientInfoBuilder {
        public RedisClientInfoBuilder() {
        }

        public static RedisClientInfo fromString(String source) {
            Assert.notNull(source, "Cannot read client properties form 'null'.");
            Properties properties = new Properties();

            try {
                properties.load(new StringReader(source.replace(' ', '\n')));
            } catch (IOException var3) {
                throw new IllegalArgumentException(String.format("Properties could not be loaded from String '%s'.", source), var3);
            }

            return new RedisClientInfo(properties);
        }
    }

    public enum INFO {
        ADDRESS_PORT("addr"),
        FILE_DESCRIPTOR("fd"),
        CONNECTION_NAME("name"),
        CONNECTION_AGE("age"),
        CONNECTION_IDLE("idle"),
        FLAGS("flags"),
        DATABSE_ID("db"),
        CHANNEL_SUBSCRIBTIONS("sub"),
        PATTERN_SUBSCRIBTIONS("psub"),
        MULIT_COMMAND_CONTEXT("multi"),
        BUFFER_LENGTH("qbuf"),
        BUFFER_FREE_SPACE("qbuf-free"),
        OUTPUT_BUFFER_LENGTH("obl"),
        OUTPUT_LIST_LENGTH("oll"),
        OUTPUT_BUFFER_MEMORY_USAGE("omem"),
        EVENTS("events"),
        LAST_COMMAND("cmd");

        final String key;

        private INFO(String key) {
            this.key = key;
        }
    }
}
