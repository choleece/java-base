package cn.choleece.base.framework.redis.connection;

import java.util.List;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 10:18
 */
public interface RedisTxCommands {

    /**
     * Mark the start of a transaction block. <br>
     * Commands will be queued and can then be executed by calling {@link #exec()} or rolled back using {@link #discard()}
     * <p>
     *
     * @see <a href="https://redis.io/commands/multi">Redis Documentation: MULTI</a>
     */
    void multi();

    /**
     * Executes all queued commands in a transaction started with {@link #multi()}. <br>
     * If used along with {@link #watch(byte[]...)} the operation will fail if any of watched keys has been modified.
     *
     * @return List of replies for each executed command.
     * @see <a href="https://redis.io/commands/exec">Redis Documentation: EXEC</a>
     */
    List<Object> exec();

    /**
     * Discard all commands issued after {@link #multi()}.
     *
     * @see <a href="https://redis.io/commands/discard">Redis Documentation: DISCARD</a>
     */
    void discard();

    /**
     * Watch given {@code keys} for modifications during transaction started with {@link #multi()}.
     *
     * @param keys must not be {@literal null}.
     * @see <a href="https://redis.io/commands/watch">Redis Documentation: WATCH</a>
     */
    void watch(byte[]... keys);

    /**
     * Flushes all the previously {@link #watch(byte[]...)} keys.
     *
     * @see <a href="https://redis.io/commands/unwatch">Redis Documentation: UNWATCH</a>
     */
    void unwatch();
}
