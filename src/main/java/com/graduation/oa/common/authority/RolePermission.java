package com.graduation.oa.common.authority;

import java.io.Serializable;
import java.util.List;

public class RolePermission implements Serializable {
    private static final long serialVersionUID = 1L;
    private String route;
    private List<String> operates;
    private List<String> urls;

    public RolePermission() {
    }

    public String getRoute() {
        return this.route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<String> getOperates() {
        return this.operates;
    }

    public void setOperates(List<String> operates) {
        this.operates = operates;
    }

    public List<String> getUrls() {
        return this.urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
