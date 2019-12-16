package com.graduation.oa.common.authority;

public class AuthorityContext {
    private static ThreadLocal<String> scopeThread = new ThreadLocal();
    private static ThreadLocal<String> authoritiesThread = new ThreadLocal();

    public AuthorityContext() {
    }

    public static String getScope() {
        return (String)scopeThread.get();
    }

    public static void setScope(String scope) {
        scopeThread.set(scope);
    }

    public static String getAuthorities() {
        return (String)authoritiesThread.get();
    }

    public static void setAuthorities(String authorities) {
        authoritiesThread.set(authorities);
    }
}
