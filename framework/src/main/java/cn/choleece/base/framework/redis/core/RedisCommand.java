package cn.choleece.base.framework.redis.core;

import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author choleece
 * @Description: Redis 命令
 * @Date 2019-10-15 22:11
 **/
public enum RedisCommand {

    APPEND("rw", 2, 2),
    AUTH("rw", 1, 1),
    BGREWRITEAOF("r", 0, 0, new String[]{"bgwriteaof"}),
    BGSAVE("r", 0, 0),
    BITCOUNT("r", 1, 3),
    BITOP("rw", 3),
    BITPOS("r", 2, 4),
    BLPOP("rw", 2),
    BRPOP("rw", 2),
    BRPOPLPUSH("rw", 3),
    CLIENT_KILL("rw", 1, 1),
    CLIENT_LIST("r", 0, 0),
    CLIENT_GETNAME("r", 0, 0),
    CLIENT_PAUSE("rw", 1, 1),
    CLIENT_SETNAME("w", 1, 1),
    CONFIG_GET("r", 1, 1, new String[]{"getconfig"}),
    CONFIG_REWRITE("rw", 0, 0),
    CONFIG_SET("w", 2, 2, new String[]{"setconfig"}),
    CONFIG_RESETSTAT("w", 0, 0, new String[]{"resetconfigstats"}),
    DBSIZE("r", 0, 0),
    DECR("w", 1, 1),
    DECRBY("w", 2, 2),
    DEL("rw", 1),
    DISCARD("rw", 0, 0),
    DUMP("r", 1, 1),
    ECHO("r", 1, 1),
    EVAL("rw", 2),
    EVALSHA("rw", 2),
    EXEC("rw", 0, 0),
    EXISTS("r", 1, 1),
    EXPIRE("rw", 2, 2),
    EXPIREAT("rw", 2, 2),
    FLUSHALL("w", 0, 0),
    FLUSHDB("w", 0, 0),
    GET("r", 1, 1),
    GETBIT("r", 2, 2),
    GETRANGE("r", 3, 3),
    GETSET("rw", 2, 2),
    GEOADD("w", 3),
    GEODIST("r", 2),
    GEOHASH("r", 2),
    GEOPOS("r", 2),
    GEORADIUS("r", 4),
    GEORADIUSBYMEMBER("r", 3),
    HDEL("rw", 2),
    HEXISTS("r", 2, 2),
    HGET("r", 2, 2),
    HGETALL("r", 1, 1),
    HINCRBY("rw", 3, 3),
    HINCBYFLOAT("rw", 3, 3),
    HKEYS("r", 1),
    HLEN("r", 1),
    HMGET("r", 2),
    HMSET("w", 3),
    HSET("w", 3, 3),
    HSETNX("w", 3, 3),
    HVALS("r", 1, 1),
    INCR("rw", 1),
    INCRBYFLOAT("rw", 2, 2),
    INFO("r", 0),
    KEYS("r", 1),
    LASTSAVE("r", 0),
    LINDEX("r", 2, 2),
    LINSERT("rw", 4, 4),
    LLEN("r", 1, 1),
    LPOP("rw", 1, 1),
    LPUSH("rw", 2),
    LPUSHX("rw", 2),
    LRANGE("r", 3, 3),
    LREM("rw", 3, 3),
    LSET("w", 3, 3),
    LTRIM("w", 3, 3),
    MGET("r", 1),
    MIGRATE("rw", 0),
    MONITOR("rw", 0, 0),
    MOVE("rw", 2, 2),
    MSET("w", 2),
    MSETNX("w", 2),
    MULTI("rw", 0, 0),
    PERSIST("rw", 1, 1),
    PEXPIRE("rw", 2, 2),
    PEXPIREAT("rw", 2, 2),
    PING("r", 0, 0),
    PSETEX("w", 3),
    PSUBSCRIBE("r", 1),
    PTTL("r", 1, 1),
    QUIT("rw", 0, 0),
    RANDOMKEY("r", 0, 0),
    RANAME("w", 2, 2),
    RENAMENX("w", 2, 2),
    RESTORE("w", 3, 3),
    RPOP("rw", 1, 1),
    RPOPLPUSH("rw", 2, 2),
    RPUSH("rw", 2),
    RPUSHX("rw", 2, 2),
    SADD("rw", 2),
    SAVE("rw", 0, 0),
    SCARD("r", 1, 1),
    SCRIPT_EXISTS("r", 1),
    SCRIPT_FLUSH("rw", 0, 0),
    SCRIPT_KILL("rw", 0, 0),
    SCRIPT_LOAD("rw", 1, 1),
    SDIFF("r", 1),
    SDIFFSTORE("rw", 2),
    SELECT("rw", 1, 1),
    SET("w", 2),
    SETBIT("rw", 3, 3),
    SETEX("w", 3, 3),
    SETNX("w", 2, 2),
    SETRANGE("rw", 3, 3),
    SHUTDOWN("rw", 0),
    SINTER("r", 1),
    SINTERSTORE("rw", 2),
    SISMEMBER("r", 2),
    SLAVEOF("w", 2),
    SLOWLOG("rw", 1),
    SMEMBERS("r", 1, 1),
    SMOVE("rw", 3, 3),
    SORT("rw", 1),
    SPOP("rw", 1, 1),
    SRANDMEMBER("r", 1, 1),
    SREM("rw", 2),
    STRLEN("r", 1, 1),
    SUBSCRIBE("rw", 1),
    SUNION("r", 1),
    SUNIONSTORE("rw ", 2),
    SYNC("rw", 0, 0),
    TIME("r", 0, 0),
    TTL("r", 1, 1),
    TYPE("r", 1, 1),
    UNSUBSCRIBE("rw", 0),
    UNWATCH("rw", 0, 0),
    WATCH("rw", 1),
    ZADD("rw", 3),
    ZCARD("r", 1),
    ZCOUNT("r", 3, 3),
    ZINCRBY("rw", 3),
    ZINTERSTORE("rw", 3),
    ZRANGE("r", 3),
    ZRANGEBYSCORE("r", 3),
    ZRANK("r", 2, 2),
    ZREM("rw", 2),
    ZREMRANGEBYRANK("rw", 3, 3),
    ZREMRANGEBYSCORE("rm", 3, 3),
    ZREVRANGE("r", 3),
    ZREVRANGEBYSCORE("r", 3),
    ZREVRANK("r", 2, 2),
    ZSCORE("r", 2, 2),
    ZUNIONSTORE("rw", 3),
    SCAN("r", 1),
    SSCAN("r", 2),
    HSCAN("r", 2),
    ZSCAN("r", 2),
    UNKNOWN("rw", -1);

    private boolean read;
    private boolean write;
    private Set<String> alias;
    private int minArgs;
    private int maxArgs;
    private static final Map<String, RedisCommand> commandLookup = buildCommandLookupTable();

    private static Map<String, RedisCommand> buildCommandLookupTable() {
        RedisCommand[] cmds = values();
        Map<String, RedisCommand> map = new HashMap(cmds.length, 1.0F);
        RedisCommand[] var2 = cmds;
        int var3 = cmds.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            RedisCommand cmd = var2[var4];
            map.put(cmd.name().toLowerCase(), cmd);
            Iterator var6 = cmd.alias.iterator();

            while(var6.hasNext()) {
                String alias = (String)var6.next();
                map.put(alias, cmd);
            }
        }

        return Collections.unmodifiableMap(map);
    }

    private RedisCommand(String mode, int minArgs) {
        this(mode, minArgs, -1);
    }

    private RedisCommand(String mode, int minArgs, int maxArgs) {
        this.read = true;
        this.write = true;
        this.alias = new HashSet(1);
        this.minArgs = -1;
        this.maxArgs = -1;
        if (StringUtils.hasText(mode)) {
            this.read = mode.toLowerCase().indexOf(114) > -1;
            this.write = mode.toLowerCase().indexOf(119) > -1;
        }

        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    private RedisCommand(String mode, int minArgs, int maxArgs, String... alias) {
        this(mode, minArgs, maxArgs);
        if (alias.length > 0) {
            this.alias.addAll(Arrays.asList(alias));
        }

    }

    public boolean requiresArguments() {
        return this.minArgs >= 0;
    }

    public boolean requiresExactNumberOfArguments() {
        return this.maxArgs == 0 || this.minArgs == this.maxArgs;
    }

    public boolean isRead() {
        return this.read;
    }

    public boolean isWrite() {
        return this.write;
    }

    public boolean isReadonly() {
        return this.read && !this.write;
    }

    public boolean isRepresentedBy(String command) {
        if (!StringUtils.hasText(command)) {
            return false;
        } else {
            return this.toString().equalsIgnoreCase(command) ? true : this.alias.contains(command.toLowerCase());
        }
    }

    public void validateArgumentCount(int nrArguments) {
        if (this.requiresArguments()) {
            if (this.requiresExactNumberOfArguments() && nrArguments != this.maxArgs) {
                throw new IllegalArgumentException(String.format("%s command requires %s %s.", this.name(), this.maxArgs, arguments(this.maxArgs)));
            }

            if (nrArguments < this.minArgs) {
                throw new IllegalArgumentException(String.format("%s command requires at least %s %s.", this.name(), this.minArgs, arguments(this.maxArgs)));
            }

            if (this.maxArgs > 0 && nrArguments > this.maxArgs) {
                throw new IllegalArgumentException(String.format("%s command requires at most %s %s.", this.name(), this.maxArgs, arguments(this.maxArgs)));
            }
        }

    }

    private static String arguments(int count) {
        return count == 1 ? "argument" : "arguments";
    }

    public static RedisCommand failsafeCommandLookup(String key) {
        if (!StringUtils.hasText(key)) {
            return UNKNOWN;
        } else {
            RedisCommand cmd = (RedisCommand)commandLookup.get(key.toLowerCase());
            return cmd != null ? cmd : UNKNOWN;
        }
    }
}
