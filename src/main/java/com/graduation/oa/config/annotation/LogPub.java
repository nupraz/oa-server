package com.graduation.oa.config.annotation;

import java.lang.annotation.*;

/**
 * @Classname Log
 * @Description
 * @Date 2019/11/6 11:41
 * @Created by yl
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogPub {
    /**
     * 日志打印时方法名
     */
    String value();

    /**
     * 是否打印接口用时日志,默认打印
     */
    boolean timeLog() default true;

    /**
     * 是否打印接口请求参数返回参数,默认打印
     */
    boolean paramsLog() default true;
}
