package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.Cursor;
import cn.choleece.base.framework.redis.core.ScanOptions;
import cn.choleece.base.framework.redis.core.types.Expiration;
import cn.choleece.base.framework.redis.core.types.RedisClientInfo;
import org.springframework.lang.Nullable;
import sun.reflect.generics.tree.ReturnType;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 09:39
 */
public interface StringRedisConnection extends RedisConnection {

    Object execute(String var1, String... var2);

    Object execute(String var1);

    Boolean exists(String var1);

    @Nullable
    Long exists(String... var1);

    Long del(String... var1);

    @Nullable
    Long unlink(String... var1);

    DataType type(String var1);

    @Nullable
    Long touch(String... var1);

    Collection<String> keys(String var1);

    void rename(String var1, String var2);

    Boolean renameNX(String var1, String var2);

    Boolean expire(String var1, long var2);

    Boolean pExpire(String var1, long var2);

    Boolean expireAt(String var1, long var2);

    Boolean pExpireAt(String var1, long var2);

    Boolean persist(String var1);

    Boolean move(String var1, int var2);

    Long ttl(String var1);

    Long ttl(String var1, TimeUnit var2);

    Long pTtl(String var1);

    Long pTtl(String var1, TimeUnit var2);

    String echo(String var1);

    List<String> sort(String var1, SortParameters var2);

    Long sort(String var1, SortParameters var2, String var3);

    @Nullable
    ValueEncoding encodingOf(String var1);

    @Nullable
    Duration idletime(String var1);

    @Nullable
    Long refcount(String var1);

    String get(String var1);

    String getSet(String var1, String var2);

    List<String> mGet(String... var1);

    @Nullable
    Boolean set(String var1, String var2);

    @Nullable
    Boolean set(String var1, String var2, Expiration var3, RedisStringCommands.SetOption var4);

    @Nullable
    Boolean setNX(String var1, String var2);

    @Nullable
    Boolean setEx(String var1, long var2, String var4);

    @Nullable
    Boolean pSetEx(String var1, long var2, String var4);

    @Nullable
    Boolean mSetString(Map<String, String> var1);

    Boolean mSetNXString(Map<String, String> var1);

    Long incr(String var1);

    Long incrBy(String var1, long var2);

    Double incrBy(String var1, double var2);

    Long decr(String var1);

    Long decrBy(String var1, long var2);

    Long append(String var1, String var2);

    String getRange(String var1, long var2, long var4);

    void setRange(String var1, String var2, long var3);

    Boolean getBit(String var1, long var2);

    Boolean setBit(String var1, long var2, boolean var4);

    Long bitCount(String var1);

    Long bitCount(String var1, long var2, long var4);

    Long bitOp(RedisStringCommands.BitOperation var1, String var2, String... var3);

    Long strLen(String var1);

    Long rPush(String var1, String... var2);

    Long lPush(String var1, String... var2);

    Long rPushX(String var1, String var2);

    Long lPushX(String var1, String var2);

    Long lLen(String var1);

    List<String> lRange(String var1, long var2, long var4);

    void lTrim(String var1, long var2, long var4);

    String lIndex(String var1, long var2);

    void lSet(String var1, long var2, String var4);

    Long lRem(String var1, long var2, String var4);

    String lPop(String var1);

    String rPop(String var1);

    List<String> bLPop(int var1, String... var2);

    List<String> bRPop(int var1, String... var2);

    String rPopLPush(String var1, String var2);

    String bRPopLPush(int var1, String var2, String var3);

    Long sAdd(String var1, String... var2);

    Long sRem(String var1, String... var2);

    String sPop(String var1);

    List<String> sPop(String var1, long var2);

    Boolean sMove(String var1, String var2, String var3);

    Long sCard(String var1);

    Boolean sIsMember(String var1, String var2);

    Set<String> sInter(String... var1);

    Long sInterStore(String var1, String... var2);

    Set<String> sUnion(String... var1);

    Long sUnionStore(String var1, String... var2);

    Set<String> sDiff(String... var1);

    Long sDiffStore(String var1, String... var2);

    Set<String> sMembers(String var1);

    String sRandMember(String var1);

    List<String> sRandMember(String var1, long var2);

    Cursor<String> sScan(String var1, ScanOptions var2);

    Boolean zAdd(String var1, double var2, String var4);

    Long zAdd(String var1, Set<StringRedisConnection.StringTuple> var2);

    Long zRem(String var1, String... var2);

    Double zIncrBy(String var1, double var2, String var4);

    Long zRank(String var1, String var2);

    Long zRevRank(String var1, String var2);

    Set<String> zRange(String var1, long var2, long var4);

    Set<StringRedisConnection.StringTuple> zRangeWithScores(String var1, long var2, long var4);

    Set<String> zRangeByScore(String var1, double var2, double var4);

    Set<StringRedisConnection.StringTuple> zRangeByScoreWithScores(String var1, double var2, double var4);

    Set<String> zRangeByScore(String var1, double var2, double var4, long var6, long var8);

    Set<StringRedisConnection.StringTuple> zRangeByScoreWithScores(String var1, double var2, double var4, long var6, long var8);

    Set<String> zRevRange(String var1, long var2, long var4);

    Set<StringRedisConnection.StringTuple> zRevRangeWithScores(String var1, long var2, long var4);

    Set<String> zRevRangeByScore(String var1, double var2, double var4);

    Set<StringRedisConnection.StringTuple> zRevRangeByScoreWithScores(String var1, double var2, double var4);

    Set<String> zRevRangeByScore(String var1, double var2, double var4, long var6, long var8);

    Set<StringRedisConnection.StringTuple> zRevRangeByScoreWithScores(String var1, double var2, double var4, long var6, long var8);

    Long zCount(String var1, double var2, double var4);

    Long zCard(String var1);

    Double zScore(String var1, String var2);

    Long zRemRange(String var1, long var2, long var4);

    Long zRemRangeByScore(String var1, double var2, double var4);

    Long zUnionStore(String var1, String... var2);

    Long zUnionStore(String var1, RedisZSetCommands.Aggregate var2, int[] var3, String... var4);

    Long zInterStore(String var1, String... var2);

    Long zInterStore(String var1, RedisZSetCommands.Aggregate var2, int[] var3, String... var4);

    Cursor<StringRedisConnection.StringTuple> zScan(String var1, ScanOptions var2);

    Set<String> zRangeByScore(String var1, String var2, String var3);

    Set<String> zRangeByScore(String var1, String var2, String var3, long var4, long var6);

    Set<String> zRangeByLex(String var1);

    Set<String> zRangeByLex(String var1, RedisZSetCommands.Range var2);

    Set<String> zRangeByLex(String var1, RedisZSetCommands.Range var2, RedisZSetCommands.Limit var3);

    Boolean hSet(String var1, String var2, String var3);

    Boolean hSetNX(String var1, String var2, String var3);

    String hGet(String var1, String var2);

    List<String> hMGet(String var1, String... var2);

    void hMSet(String var1, Map<String, String> var2);

    Long hIncrBy(String var1, String var2, long var3);

    Double hIncrBy(String var1, String var2, double var3);

    Boolean hExists(String var1, String var2);

    Long hDel(String var1, String... var2);

    Long hLen(String var1);

    Set<String> hKeys(String var1);

    List<String> hVals(String var1);

    Map<String, String> hGetAll(String var1);

    Cursor<Map.Entry<String, String>> hScan(String var1, ScanOptions var2);

    @Nullable
    Long hStrLen(String var1, String var2);

    Long pfAdd(String var1, String... var2);

    Long pfCount(String... var1);

    void pfMerge(String var1, String... var2);

    Long publish(String var1, String var2);

    void subscribe(MessageListener var1, String... var2);

    void pSubscribe(MessageListener var1, String... var2);

    String scriptLoad(String var1);

    <T> T eval(String var1, ReturnType var2, int var3, String... var4);

    <T> T evalSha(String var1, ReturnType var2, int var3, String... var4);

    void setClientName(String var1);

    @Override
    List<RedisClientInfo> getClientList();

    List<Long> bitfield(String var1, BitFieldSubCommands var2);

    public interface StringTuple extends RedisZSetCommands.Tuple {
        String getValueAsString();
    }

}
