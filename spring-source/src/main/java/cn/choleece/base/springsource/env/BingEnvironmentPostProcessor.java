package cn.choleece.base.springsource.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * 允许自定义properties
 *
 * @author choleece
 * @Description: order 的顺序决定是否阔以被外层覆盖，如果权重选最大，那么将无法被覆盖，
 * 一般选择Ordered.LOWEST_PRECEDENCE(默认是这个)， 这样阔以在最外层被覆盖
 * @Date 2020-12-17 22:58
 **/
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class BingEnvironmentPostProcessor implements EnvironmentPostProcessor {

    public static final String PROPERTY_SOURCE_NAME = "bing-default";

    public static final String RESOURCE_LOCATION_PATTERN = CLASSPATH_ALL_URL_PREFIX
            + "META-INF/bing-default.properties";

    private static final String FILE_ENCODING = "UTF-8";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ResourceLoader resourceLoader = getResourceLoader(application);

        processPropertySource(environment, resourceLoader);

    }

    private ResourceLoader getResourceLoader(SpringApplication application) {

        ResourceLoader resourceLoader = application.getResourceLoader();

        if (resourceLoader == null) {
            resourceLoader = new DefaultResourceLoader(application.getClassLoader());
        }

        return resourceLoader;
    }

    private void processPropertySource(ConfigurableEnvironment environment, ResourceLoader resourceLoader) {

        try {
            PropertySource bingPropertySource = buildPropertySource(resourceLoader);
            MutablePropertySources propertySources = environment.getPropertySources();
            // 这一步是将其添加到系统环境
            propertySources.addLast(bingPropertySource);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private PropertySource buildPropertySource(ResourceLoader resourceLoader) throws IOException {
        CompositePropertySource propertySource = new CompositePropertySource(PROPERTY_SOURCE_NAME);
        appendPropertySource(propertySource, resourceLoader);
        return propertySource;
    }

    private void appendPropertySource(CompositePropertySource propertySource, ResourceLoader resourceLoader)
            throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
        Resource[] resources = resourcePatternResolver.getResources(RESOURCE_LOCATION_PATTERN);
        for (Resource resource : resources) {
            // Add if exists
            if (resource.exists()) {
                String internalName = String.valueOf(resource.getURL());
                propertySource.addPropertySource(new ResourcePropertySource(internalName,
                        new EncodedResource(resource, FILE_ENCODING)));
            }
        }
    }
}
