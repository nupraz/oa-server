package com.graduation.oa.common.security;

import com.graduation.oa.common.exception.AuthorityException;
import com.graduation.oa.common.redis.Cache;
import com.graduation.oa.common.util.HttpUtils;
import com.graduation.oa.common.support.Annotation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * authentication aop class.
 * 鉴权拦截器，处理过程如下：
 * 读取resource/authority.json权限配置文件，拦截所有的RequestMapping，校验用户是否有接口请求权限
 *
 * @author Liu qingxiang
 * @since v1.0.0
 */
@Aspect
@Component
public class AuthorityInterceptor {
    public Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    @Qualifier("authorityCache")
    private Cache authorityCache;

    @Autowired
    private Cache cache;

    @Value("${app.authority.check-url}")
    private Boolean checkUrl;

    @Pointcut("execution(* com.bestvike..*.*(..)) && "
            + "(  @annotation(org.springframework.web.bind.annotation.PostMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.GetMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"
            + "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    private void doRequestPointcut() {

    }

    @Before(value = "doRequestPointcut()")
    public void doAuth(JoinPoint joinPoint) throws NoSuchMethodException {

        Class[] requestTypeArray = { GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class };
        List<Class> requestTypeList = Arrays.asList(requestTypeArray);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        java.lang.annotation.Annotation[] annotations = method.getAnnotations();
        Optional<java.lang.annotation.Annotation> find = Arrays.stream(annotations)
                .filter(annotation -> requestTypeList.contains(annotation.annotationType())).findFirst();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (!checkUrl || !find.isPresent())
            return;
        java.lang.annotation.Annotation typeAnnotation = find.get();
        Class cl = typeAnnotation.getClass();
        Method method1 = cl.getMethod("value");
        try {
            // 兼容类注解RequestMapping的情况
            RequestMapping requestMapping = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
            String value = "";
            if (requestMapping != null)
                value = requestMapping.value()[0];
            String[] methodValue = (String[]) method1.invoke(typeAnnotation);
            if (methodValue != null) {
                value += methodValue[0];
            }
            // 请求路径，避免双斜杠可能性

            value.replaceAll("//", "/");
            // 获取请求类型
            String requestType = typeAnnotation.annotationType().getTypeName();
            if (StringUtils.isEmpty(requestType)) {
                // 异常
                logger.error("没有请求类型");
                throw new AuthorityException("鉴权失败");
            }
            // 鉴权
            String methodName = Annotation.getMethod(requestType);

            if (!cache.exists("authority_config:__timestamp__")) {
                // 异常
                logger.error("没有读取到鉴权配置");
                throw new AuthorityException("鉴权失败");
            }

            List<String> whiteList = cache.get("authority_config:whiteList", List.class);
            if (whiteList == null || !(whiteList.contains(methodName + ":" + value) || whiteList.contains(value))) {
                String token = HttpUtils.getToken(request);
                if (!StringUtils.isEmpty(token)) {
                    String userId = authorityCache.get("token_to_user:" + token, String.class);
                    if (!StringUtils.isEmpty(userId)) {
                        if (!isAuth(userId, methodName, value)) {
                            logger.error("非法的请求 ==> " + methodName + ":" + value);
                            throw new AuthorityException("鉴权失败");
                        }
                    } else {
                        // 异常
                        logger.error("获取不到用户id");
                        throw new AuthorityException("鉴权失败");
                    }
                } else {
                    // 异常
                    logger.error("获取不到token");
                    throw new AuthorityException("鉴权失败");
                }
                /*OAuth2Authentication authentication = tokenStore.readAuthentication(token);
                if (authentication == null || authentication.getName() == null) {
                    // 异常
                    logger.error("获取不到用户id");
                    throw new AuthorityException("鉴权失败");
                }

                if (!isAuth(authentication.getName(), methodName, value)) {
                    logger.error("非法的请求 ==> " + methodName + ":" + value);
                    throw new AuthorityException("鉴权失败");
                }*/
            }
        } catch (IllegalAccessException e) {
            logger.error(e);
        } catch (InvocationTargetException e) {
            logger.error(e);
        }

    }

    public boolean isAuth(String userId, String method, String url) {
        if (!checkUrl) {
            return true;
        }
        HashSet<String> userPermissions = authorityCache.get("user_permissions:" + userId, HashSet.class);
        if (userPermissions == null) {
            return false;
        }
        return userPermissions.contains(method + ":" + url) || userPermissions.contains(url);
    }
}
