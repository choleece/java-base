package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.redis.connection.jedis.JedisClientConfiguration;

/**
 * @author choleece
 * @Description: 函数式接口
 * 所谓的函数式接口，当然首先是一个接口，然后就是在这个接口里面只能有一个抽象方法。
 *
 * 这种类型的接口也称为SAM接口，即Single Abstract Method interfaces
 * 参考：https://www.jianshu.com/p/52cdc402fb5d
 * @Date 2019-10-13 23:21
 **/
@FunctionalInterface
public interface JedisClientConfigurationBuilderCustomizer {

    void customize(JedisClientConfiguration.JedisClientConfigurationBuilder clientConfigurationBuilder);
}
