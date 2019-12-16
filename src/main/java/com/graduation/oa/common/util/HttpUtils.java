package com.graduation.oa.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public final class HttpUtils {
    public HttpUtils() {
    }

    public static String getToken(HttpServletRequest httpServletRequest) {
        String token = null;
        String authorization = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null && cookies.length > 0) {
                Cookie[] var4 = cookies;
                int var5 = cookies.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Cookie cookie = var4[var6];
                    if (cookie.getName().equalsIgnoreCase("X-Token")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        return token;
    }
}
