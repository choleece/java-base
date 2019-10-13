package cn.choleece.base.framework.redis.connection.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.lang.Nullable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.time.Duration;
import java.util.Optional;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-13 23:32
 **/
public class DefaultJedisClientConfiguration implements JedisClientConfiguration {

    private final boolean useSsl;
    private final Optional<SSLSocketFactory> sslSocketFactory;
    private final Optional<SSLParameters> sslParameters;
    private final Optional<HostnameVerifier> hostnameVerifier;
    private final boolean usePooling;
    private final Optional<GenericObjectPoolConfig> poolConfig;
    private final Optional<String> clientName;
    private final Duration readTimeout;
    private final Duration connectTimeout;

    DefaultJedisClientConfiguration(boolean useSsl, @Nullable SSLSocketFactory sslSocketFactory, @Nullable SSLParameters sslParameters, @Nullable HostnameVerifier hostnameVerifier, boolean usePooling, @Nullable GenericObjectPoolConfig poolConfig, @Nullable String clientName, Duration readTimeout, Duration connectTimeout) {
        this.useSsl = useSsl;
        this.sslSocketFactory = Optional.ofNullable(sslSocketFactory);
        this.sslParameters = Optional.ofNullable(sslParameters);
        this.hostnameVerifier = Optional.ofNullable(hostnameVerifier);
        this.usePooling = usePooling;
        this.poolConfig = Optional.ofNullable(poolConfig);
        this.clientName = Optional.ofNullable(clientName);
        this.readTimeout = readTimeout;
        this.connectTimeout = connectTimeout;
    }

    @Override
    public boolean isUseSsl() {
        return this.useSsl;
    }

    @Override
    public Optional<SSLSocketFactory> getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    @Override
    public Optional<SSLParameters> getSslParameters() {
        return this.sslParameters;
    }

    @Override
    public Optional<HostnameVerifier> getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    @Override
    public boolean isUsePooling() {
        return this.usePooling;
    }

    @Override
    public Optional<GenericObjectPoolConfig> getPoolConfig() {
        return this.poolConfig;
    }

    @Override
    public Optional<String> getClientName() {
        return this.clientName;
    }

    @Override
    public Duration getReadTimeout() {
        return this.readTimeout;
    }

    @Override
    public Duration getConnectTimeout() {
        return this.connectTimeout;
    }
}
