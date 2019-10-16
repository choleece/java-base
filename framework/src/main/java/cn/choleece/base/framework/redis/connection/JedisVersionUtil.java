package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.Version;
import cn.choleece.base.framework.redis.VersionParser;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Properties;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 16:17
 */
public class JedisVersionUtil {

    private static Version jedisVersion = parseVersion(resolveJedisVersion());

    /**
     * @return current {@link redis.clients.jedis.Jedis} version.
     */
    public static Version jedisVersion() {
        return jedisVersion;
    }

    /**
     * Parse version string {@literal eg. 1.1.1} to {@link Version}.
     *
     * @param version
     * @return
     */
    static Version parseVersion(String version) {
        return VersionParser.parseVersion(version);
    }

    /**
     * @return true if used jedis version is at minimum {@literal 2.4}.
     */
    public static boolean atLeastJedis24() {
        return atLeast("2.4");
    }

    private static String resolveJedisVersion() {

        String version = Jedis.class.getPackage().getImplementationVersion();

        if (!StringUtils.hasText(version)) {
            try {
                Properties props = PropertiesLoaderUtils.loadAllProperties("META-INF/maven/redis.clients/jedis/pom.properties");
                if (props.containsKey("version")) {
                    version = props.getProperty("version");
                }
            } catch (IOException e) {
                // ignore this one
            }
        }
        return version;
    }

    /**
     * Compares given version string against current jedis version.
     *
     * @param version
     * @return true in case given version is greater than equal to current one.
     */
    public static boolean atLeast(String version) {
        return jedisVersion.compareTo(parseVersion(version)) >= 0;
    }

    /**
     * Compares given version string against current jedis version.
     *
     * @param version
     * @return true in case given version is less than equal to current one.
     */
    public static boolean atMost(String version) {
        return jedisVersion.compareTo(parseVersion(version)) <= 0;
    }
}