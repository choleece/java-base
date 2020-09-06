package cn.choleece.base.md.redis.distribute.aspect;

import cn.choleece.base.md.redis.distribute.annotation.SpringControllerLimit;
import cn.choleece.base.md.redis.distribute.limit.RedisLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-06 13:36
 **/
@Component
public class WebIntercept implements WebMvcConfigurer {

    private static Logger logger = LoggerFactory.getLogger(WebIntercept.class);

    @Resource
    private RedisLimit redisLimit;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomInterceptor())
                .addPathPatterns("/**");
    }

    private class CustomInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (redisLimit == null) {
                throw new NullPointerException("redisLimit is null");
            }

            if (handler instanceof HandlerMethod) {
                HandlerMethod method = (HandlerMethod) handler;

                SpringControllerLimit annotation = method.getMethodAnnotation(SpringControllerLimit.class);
                if (annotation == null) {
                    // skip
                    return true;
                }
                boolean limit = redisLimit.limit();
                if (!limit) {
                    logger.info("request has bean limited");
                    response.sendError(annotation.errorCode(), "request limited");
                    return false;
                }
            }
            return true;
        }
    }
}
