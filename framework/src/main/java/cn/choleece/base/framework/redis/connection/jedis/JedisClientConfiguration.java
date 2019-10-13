package cn.choleece.base.framework.redis.connection.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPoolConfig;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.time.Duration;
import java.util.Optional;

/**
 * @author choleece
 * @Description: Jedis 客户端连接配置
 * @Date 2019-10-13 23:25
 **/
public interface JedisClientConfiguration {

    boolean isUseSsl();

    Optional<SSLSocketFactory> getSslSocketFactory();

    Optional<SSLParameters> getSslParameters();

    Optional<HostnameVerifier> getHostnameVerifier();

    boolean isUsePooling();

    Optional<GenericObjectPoolConfig> getPoolConfig();

    Optional<String> getClientName();

    Duration getConnectTimeout();

    Duration getReadTimeout();

    static JedisClientConfiguration.JedisClientConfigurationBuilder builder() {
        return new JedisClientConfiguration.DefaultJedisClientConfigurationBuilder();
    }

    static JedisClientConfiguration defaultConfiguration() {
        return builder().build();
    }

    public static class DefaultJedisClientConfigurationBuilder implements JedisClientConfiguration.JedisClientConfigurationBuilder, JedisClientConfiguration.JedisPoolingClientConfigurationBuilder, JedisClientConfiguration.JedisSslClientConfigurationBuilder {
        private boolean useSsl;
        @Nullable
        private SSLSocketFactory sslSocketFactory;
        @Nullable
        private SSLParameters sslParameters;
        @Nullable
        private HostnameVerifier hostnameVerifier;
        private boolean usePooling;
        private GenericObjectPoolConfig poolConfig;
        @Nullable
        private String clientName;
        private Duration readTimeout;
        private Duration connectTimeout;

        private DefaultJedisClientConfigurationBuilder() {
            this.poolConfig = new JedisPoolConfig();
            this.readTimeout = Duration.ofMillis(2000L);
            this.connectTimeout = Duration.ofMillis(2000L);
        }

        @Override
        public JedisClientConfiguration.JedisSslClientConfigurationBuilder useSsl() {
            this.useSsl = true;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisSslClientConfigurationBuilder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            Assert.notNull(sslSocketFactory, "SSLSocketFactory must not be null!");
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisSslClientConfigurationBuilder sslParameters(SSLParameters sslParameters) {
            Assert.notNull(sslParameters, "SSLParameters must not be null!");
            this.sslParameters = sslParameters;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisSslClientConfigurationBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
            Assert.notNull(hostnameVerifier, "HostnameVerifier must not be null!");
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisPoolingClientConfigurationBuilder usePooling() {
            this.usePooling = true;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisPoolingClientConfigurationBuilder poolConfig(GenericObjectPoolConfig poolConfig) {
            Assert.notNull(poolConfig, "GenericObjectPoolConfig must not be null!");
            this.poolConfig = poolConfig;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisClientConfigurationBuilder and() {
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisClientConfigurationBuilder clientName(String clientName) {
            Assert.hasText(clientName, "Client name must not be null or empty!");
            this.clientName = clientName;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisClientConfigurationBuilder readTimeout(Duration readTimeout) {
            Assert.notNull(readTimeout, "Duration must not be null!");
            this.readTimeout = readTimeout;
            return this;
        }

        @Override
        public JedisClientConfiguration.JedisClientConfigurationBuilder connectTimeout(Duration connectTimeout) {
            Assert.notNull(connectTimeout, "Duration must not be null!");
            this.connectTimeout = connectTimeout;
            return this;
        }

        @Override
        public JedisClientConfiguration build() {
            return new DefaultJedisClientConfiguration(this.useSsl, this.sslSocketFactory, this.sslParameters, this.hostnameVerifier, this.usePooling, this.poolConfig, this.clientName, this.readTimeout, this.connectTimeout);
        }
    }

    interface JedisSslClientConfigurationBuilder {
        JedisClientConfiguration.JedisSslClientConfigurationBuilder sslSocketFactory(SSLSocketFactory var1);

        JedisClientConfiguration.JedisSslClientConfigurationBuilder sslParameters(SSLParameters var1);

        JedisClientConfiguration.JedisSslClientConfigurationBuilder hostnameVerifier(HostnameVerifier var1);

        JedisClientConfiguration.JedisClientConfigurationBuilder and();

        JedisClientConfiguration build();
    }

    interface JedisPoolingClientConfigurationBuilder {
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder poolConfig(GenericObjectPoolConfig var1);

        JedisClientConfiguration.JedisClientConfigurationBuilder and();

        JedisClientConfiguration build();
    }

    interface JedisClientConfigurationBuilder {
        JedisClientConfiguration.JedisSslClientConfigurationBuilder useSsl();

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder usePooling();

        JedisClientConfiguration.JedisClientConfigurationBuilder clientName(String var1);

        JedisClientConfiguration.JedisClientConfigurationBuilder readTimeout(Duration var1);

        JedisClientConfiguration.JedisClientConfigurationBuilder connectTimeout(Duration var1);

        JedisClientConfiguration build();
    }

}
