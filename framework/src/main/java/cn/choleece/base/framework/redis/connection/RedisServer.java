package cn.choleece.base.framework.redis.connection;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @description: Redis 服务
 * @author: sf
 * @time: 2019-10-14 17:05
 */
public class RedisServer extends RedisNode {

    private Properties properties;

    public RedisServer(String host, int port) {
        this(host, port, new Properties());
    }

    public RedisServer(String host, int port, Properties properties) {
        super(host, port);
        this.properties = properties;
        String name = host + ":" + port;
        if (properties != null && properties.containsKey(RedisServer.INFO.NAME.key)) {
            name = this.get(RedisServer.INFO.NAME);
        }

        this.setName(name);
    }

    public static RedisServer newServerFrom(Properties properties) {
        String host = properties.getProperty(RedisServer.INFO.HOST.key, "127.0.0.1");
        int port = Integer.parseInt(properties.getProperty(RedisServer.INFO.PORT.key, "26379"));
        return new RedisServer(host, port, properties);
    }

    public void setQuorum(Long quorum) {
        if (quorum == null) {
            this.properties.remove(RedisServer.INFO.QUORUM.key);
        } else {
            this.properties.put(RedisServer.INFO.QUORUM.key, quorum.toString());
        }
    }

    public String getRunId() {
        return this.get(RedisServer.INFO.RUN_ID);
    }

    public String getFlags() {
        return this.get(RedisServer.INFO.FLAGS);
    }

    @Override
    public boolean isMaster() {
        String role = this.getRoleReported();
        return !StringUtils.hasText(role) ? false : role.equalsIgnoreCase("master");
    }

    public Long getPendingCommands() {
        return this.getLongValueOf(RedisServer.INFO.PENDING_COMMANDS);
    }

    public Long getLastPingSent() {
        return this.getLongValueOf(RedisServer.INFO.LAST_PING_SENT);
    }

    public Long getLastOkPingReply() {
        return this.getLongValueOf(RedisServer.INFO.LAST_OK_PING_REPLY);
    }

    public Long getDownAfterMilliseconds() {
        return this.getLongValueOf(RedisServer.INFO.DOWN_AFTER_MILLISECONDS);
    }

    public Long getInfoRefresh() {
        return this.getLongValueOf(RedisServer.INFO.INFO_REFRESH);
    }

    public String getRoleReported() {
        return this.get(RedisServer.INFO.ROLE_REPORTED);
    }

    public Long roleReportedTime() {
        return this.getLongValueOf(RedisServer.INFO.ROLE_REPORTED_TIME);
    }

    public Long getConfigEpoch() {
        return this.getLongValueOf(RedisServer.INFO.CONFIG_EPOCH);
    }

    public Long getNumberSlaves() {
        return this.getNumberReplicas();
    }

    public Long getNumberReplicas() {
        return this.getLongValueOf(RedisServer.INFO.NUMBER_SLAVES);
    }

    public Long getNumberOtherSentinels() {
        return this.getLongValueOf(RedisServer.INFO.NUMBER_OTHER_SENTINELS);
    }

    public Long getQuorum() {
        return this.getLongValueOf(RedisServer.INFO.QUORUM);
    }

    public Long getFailoverTimeout() {
        return this.getLongValueOf(RedisServer.INFO.FAILOVER_TIMEOUT);
    }

    public Long getParallelSyncs() {
        return this.getLongValueOf(RedisServer.INFO.PARALLEL_SYNCS);
    }

    public String get(RedisServer.INFO info) {
        Assert.notNull(info, "Cannot retrieve client information for 'null'.");
        return this.get(info.key);
    }

    public String get(String key) {
        Assert.hasText(key, "Cannot get information for 'empty' / 'null' key.");
        return this.properties.getProperty(key);
    }

    private Long getLongValueOf(RedisServer.INFO info) {
        String value = this.get(info);
        return value == null ? null : Long.valueOf(value);
    }

    enum INFO {
        NAME("name"),
        HOST("ip"),
        PORT("port"),
        RUN_ID("runid"),
        FLAGS("flags"),
        PENDING_COMMANDS("pending-commands"),
        LAST_PING_SENT("last-ping-sent"),
        LAST_OK_PING_REPLY("last-ok-ping-reply"),
        DOWN_AFTER_MILLISECONDS("down-after-milliseconds"),
        INFO_REFRESH("info-refresh"),
        ROLE_REPORTED("role-reported"),
        ROLE_REPORTED_TIME("role-reported-time"),
        CONFIG_EPOCH("config-epoch"),
        NUMBER_SLAVES("num-slaves"),
        NUMBER_OTHER_SENTINELS("num-other-sentinels"),
        BUFFER_LENGTH("qbuf"),
        BUFFER_FREE_SPACE("qbuf-free"),
        OUTPUT_BUFFER_LENGTH("obl"),
        OUTPUT_LIST_LENGTH("number-other-sentinels"),
        QUORUM("quorum"),
        FAILOVER_TIMEOUT("failover-timeout"),
        PARALLEL_SYNCS("parallel-syncs");

        String key;

        private INFO(String key) {
            this.key = key;
        }
    }
}
