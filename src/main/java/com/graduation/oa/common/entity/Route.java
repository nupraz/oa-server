package com.graduation.oa.common.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Route implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;
    private String path;
    private String component;
    private Boolean hidden;
    private Boolean alwaysShow;
    private String redirect;
    private String name;
    private Route.Meta meta;
    private Route.Authority authority;
    private List<Route> children;

    public Route() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return this.component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Boolean getHidden() {
        return this.hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getAlwaysShow() {
        return this.alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public String getRedirect() {
        return this.redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Route.Meta getMeta() {
        return this.meta;
    }

    public void setMeta(Route.Meta meta) {
        this.meta = meta;
    }

    public Authority getAuthority() {
        return this.authority;
    }

    public void setAuthority(Route.Authority authority) {
        this.authority = authority;
    }

    public List<Route> getChildren() {
        return this.children;
    }

    public void setChildren(List<Route> children) {
        this.children = children;
    }

    public static List<Route> initRoutes(List<Route> routes, String parentPath) {
        if (routes != null && routes.size() != 0) {
            List<Route> results = new ArrayList();

            Route route;
            for(Iterator var3 = routes.iterator(); var3.hasNext(); results.add(route)) {
                route = (Route)var3.next();
                route.setId(!StringUtils.isEmpty(parentPath) ? parentPath + "/" + route.getPath() : route.getPath());
                if (route.getAuthority() != null && route.getAuthority().getUrls() != null) {
                    route.getAuthority().setUrls((List)null);
                }

                if (route.getAuthority() != null && route.getAuthority().getOperates() != null && route.getAuthority().getOperates().size() > 0) {
                    if (route.getMeta() == null) {
                        route.setMeta(new Route.Meta());
                    }

                    route.getMeta().setOperates(new ArrayList());
                }

                if (route.getChildren() != null && route.getChildren().size() > 0) {
                    route.setChildren(initRoutes(route.getChildren(), !StringUtils.isEmpty(parentPath) ? parentPath + "/" + route.getPath() : route.getPath()));
                }
            }

            return results;
        } else {
            return routes;
        }
    }

    public static class Meta implements Serializable {
        private static final long serialVersionUID = 1L;
        private String title;
        private String icon;
        private Boolean noCache;
        private Boolean affix;
        private Boolean breadcrumb;
        private String activeMenu;
        private Boolean authority;
        private List<Route.Operate> operates;

        public Meta() {
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Boolean getNoCache() {
            return this.noCache;
        }

        public void setNoCache(Boolean noCache) {
            this.noCache = noCache;
        }

        public Boolean getAffix() {
            return this.affix;
        }

        public void setAffix(Boolean affix) {
            this.affix = affix;
        }

        public Boolean getBreadcrumb() {
            return this.breadcrumb;
        }

        public void setBreadcrumb(Boolean breadcrumb) {
            this.breadcrumb = breadcrumb;
        }

        public String getActiveMenu() {
            return this.activeMenu;
        }

        public void setActiveMenu(String activeMenu) {
            this.activeMenu = activeMenu;
        }

        public Boolean getAuthority() {
            return this.authority;
        }

        public void setAuthority(Boolean authority) {
            this.authority = authority;
        }

        public List<Route.Operate> getOperates() {
            return this.operates;
        }

        public void setOperates(List<Route.Operate> operates) {
            this.operates = operates;
        }
    }

    public static class Operate implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private String label;
        private List<String> urls;

        public Operate() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<String> getUrls() {
            return this.urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

    public static class Authority implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<String> urls;
        private List<Route.Operate> operates;

        public Authority() {
        }

        public List<String> getUrls() {
            return this.urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        public List<Route.Operate> getOperates() {
            return this.operates;
        }

        public void setOperates(List<Route.Operate> operates) {
            this.operates = operates;
        }
    }
}
