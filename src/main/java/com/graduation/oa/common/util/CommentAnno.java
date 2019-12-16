package com.graduation.oa.common.util;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CommentAnno {
    String value()default "";
}
