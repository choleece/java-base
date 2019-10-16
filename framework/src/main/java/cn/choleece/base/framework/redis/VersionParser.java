package cn.choleece.base.framework.redis;

import org.springframework.lang.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 16:18
 */
public class VersionParser {

    private static final Pattern VERSION_MATCHER = Pattern.compile("([0-9]+)\\.([0-9]+)(\\.([0-9]+))?(.*)");

    /**
     * Parse version string {@literal eg. 1.1.1} to {@link Version}.
     *
     * @param version can be {@literal null}.
     * @return never {@literal null}.
     */
    public static Version parseVersion(@Nullable String version) {

        if (version == null) {
            return Version.UNKNOWN;
        }

        Matcher matcher = VERSION_MATCHER.matcher(version);
        if (matcher.matches()) {
            String major = matcher.group(1);
            String minor = matcher.group(2);
            String patch = matcher.group(4);
            return new Version(Integer.parseInt(major), minor != null ? Integer.parseInt(minor) : 0,
                    patch != null ? Integer.parseInt(patch) : 0);
        }

        return Version.UNKNOWN;
    }
}
