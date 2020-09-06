package cn.choleece.base.md.redis.command;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

/**
 * @author choleece
 * @Description: redis String 常规操作
 * @Date 2020-01-02 22:55
 **/
public class RedisStringUtils {
    static Jedis jedis = RedisConfig.jedis;

    public static void main(String[] args) {

//        setVal("name", "chaoli");
//
//        append("name", "choleece");
//
//        strLength("name");
//
//        getRange("name", 1L, 4L);

        setExpireVal("sex", "chaoli", "NX", "EX", 10L);

//        mset("name", "chaoli", "sex", "male");

//        msetNx("name1", "chaoli", "sex1", "male");

//        incr("mykey");

//        incrBy("mykey", 5);

//        incrByFloat("mykey", 5);

//        getset("mykey", "choleece");
    }

    /**
     * 获取key的内容
     * @param key
     * @return
     */
    public static String getString(String key) {
        return jedis.get(key);
    }

    /**
     * 返回append 后value的长度
     * 当key不存在时，此时的append 与set作用类似，当key存在后，append就是追加
     * @param key
     * @param value
     * @return
     */
    public static Long append(String key, String value) {
        Long appendResult = jedis.append(key, value);

        System.out.println("after append: " + getString(key));

        return appendResult;
    }

    /**
     * 返回字符串的长度
     * @param key
     * @return
     */
    public static Long strLength(String key) {
        Long strLen = jedis.strlen(key);

        System.out.println("str len: " + strLen);

        return strLen;
    }

    /**
     * 返回start offset 至 end offset之间的字符串, 从0开始，左、右全闭
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    public static String getRange(String key, Long startOffset, Long endOffset) {
        String range = jedis.getrange(key, startOffset, endOffset);
        System.out.println(range);

        return range;
    }

    /**
     * 普通的设置key 会将原有key进行覆盖
     * @param key
     * @param val
     */
    public static String setVal(String key, String val) {
        String resultSet = jedis.set(key, val);

        System.out.println(resultSet);

        return resultSet;
    }

    /**
     * set 参数
     * @param key
     * @param val
     * @param nxxx NX代表不存在时操作，XX代表存在时操作
     * @param exPx EX 代表单位为second;px为mill second
     * @param time
     * @return 操纵成功，返回OK，操纵失败,发挥null
     */
    public static String setExpireVal(String key, String val, String nxxx, String exPx, Long time) {
        String exPxResult = jedis.set(key, val, nxxx, exPx, time);

        System.out.println("exPxResult: " + exPxResult);

        return exPxResult;
    }

    /**
     * 多个key,value一起设置，会覆盖已存在的key
     * @param key1
     * @param val1
     * @param key2
     * @param val2
     * @return
     */
    public static String mset(String key1, String val1, String key2, String val2) {
        String msetResult = jedis.mset(key1, val1, key2, val2);

        System.out.println("msetResult: " + msetResult);

        return msetResult;
    }

    /**
     * 多个key,value一起设置，会覆盖已存在的key
     * @param key1
     * @param val1
     * @param key2
     * @param val2
     * @return 1 if the all the keys were set.
     *         0 if no key was set (at least one key already existed).
     */
    public static Long msetNx(String key1, String val1, String key2, String val2) {
        Long msetResult = jedis.msetnx(key1, val1, key2, val2);

        System.out.println("msetResult: " + msetResult);

        return msetResult;
    }

    /**
     * 针对key进行自增
     * @param mykey
     * @return
     */
    public static Long incr(String mykey) {
        Long incr = jedis.incr(mykey);

        System.out.println("incr: " + incr);

        return incr;
    }

    /**
     * 根据设置的步长来进行递增
     * @param key
     * @param step
     * @return
     */
    public static Long incrBy(String key, int step) {
        Long r = jedis.incrBy(key, step);

        System.out.println("incrBy: " + r);

        return r;
    }

    /**
     * 根据设置的步长来进行递增
     * @param key
     * @param step
     * @return
     */
    public static Double incrByFloat(String key, double step) {
        Double r = jedis.incrByFloat(key, step);

        System.out.println("incrBy: " + r);

        return r;
    }

    /**
     * 先获取一个值，然后再将key用val覆盖
     * @param key
     * @param val
     * @return
     */
    public static String getset(String key, String val) {
        String r = jedis.getSet(key, val);

        System.out.println("get set: " + r);

        return r;
    }

    /**
     * 改变指定位置的bit位的值 每一个字符是一个acs 码值
     * @param key
     * @param offset
     * @param val
     * @return
     */
    public static Boolean setbit(String key, long offset, boolean val) {
        Boolean r = jedis.setbit(key, offset, val);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 常用于统计统计网站的上线频率，比如签到，签到了就设置为1
     * 统计key对应的value里，bit为1的位
     * @param key
     * @return
     * https://www.cnblogs.com/liujiduo/p/10396020.html
     */
    public static Long bitcount(String key) {
        Long r = jedis.bitcount(key);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 返回key对应的 val里第一个0或者1的位置
     * @param key
     * @param val
     * @return
     */
    public static Long bitpos(String key, boolean val) {
        Long r = jedis.bitpos(key, val);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 针对key1, key2进行op操作，AND OR XOR NOT，将新的结果放到destKey里
     * @param op
     * @param destKey
     * @param key1
     * @param key2
     * @return
     */
    public static Long bitop(BitOP op, String destKey, String key1, String key2) {
        Long r = jedis.bitop(op, destKey, key1, key2);

        System.out.println("r: " + r);

        return r;
    }
}
