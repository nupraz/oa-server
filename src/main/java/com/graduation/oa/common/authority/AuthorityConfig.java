package com.graduation.oa.common.authority;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AuthorityConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<String> whites;
    private Map<String, List<String>> routes;

    public AuthorityConfig() {
    }

    public List<String> getWhites() {
        return this.whites;
    }

    public void setWhites(List<String> whites) {
        this.whites = whites;
    }

    public Map<String, List<String>> getRoutes() {
        return this.routes;
    }

    public void setRoutes(Map<String, List<String>> routes) {
        this.routes = routes;
    }
}
