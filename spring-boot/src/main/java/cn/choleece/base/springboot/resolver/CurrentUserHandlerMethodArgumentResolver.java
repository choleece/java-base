package cn.choleece.base.springboot.resolver;

import cn.choleece.base.springboot.annotation.CurrentUser;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 在方法里注解@CurrentUser，可以直接获取注解里的值
 * @author choleece
 * @date 2018/10/10
 */
public class CurrentUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.getParameterType().isAssignableFrom(LoginUser.class) && methodParameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, @Nullable ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, @Nullable WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 在这里可以操作数据库或者什么，把object返回，在注解了@CurrentUser的方法里可以直接取到值, 可以从request里取session里的值或者从request header的token里取
        // 通过从nativeWebRequest里取服务请求人的信息，然后处理，将对象返回给@CurrentUser 的对象
        return null;
    }
}
