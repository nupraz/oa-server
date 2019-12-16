package com.graduation.oa.common.entity;

import com.graduation.oa.common.util.StringUtils;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

public class BaseData implements Serializable{
    private static final long serialVersionUID = 1L;
    protected Integer modifyType;
    protected String modifyUser;
    protected Integer version;
    protected Date versionTime;
    @Transient
    protected String fuzzy;
    @Transient
    protected String fuzzyLike;
    @Transient
    protected String sort;

    public BaseData() {
    }

    public Integer getModifyType() {
        return this.modifyType;
    }

    public void setModifyType(Integer modifyType) {
        this.modifyType = modifyType;
    }

    public String getModifyUser() {
        return this.modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getVersionTime() {
        return this.versionTime;
    }

    public void setVersionTime(Date versionTime) {
        this.versionTime = versionTime;
    }

    public String getFuzzy() {
        return this.fuzzy;
    }

    public void setFuzzy(String fuzzy) {
        this.fuzzy = fuzzy;
    }

    public String getFuzzyLike() {
        return StringUtils.isEmpty(this.fuzzyLike) && !StringUtils.isEmpty(this.fuzzy) ? "%" + this.fuzzy + "%" : this.fuzzyLike;
    }

    public void setFuzzyLike(String fuzzyLike) {
        this.fuzzyLike = fuzzyLike;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Sort formatSort() {
        if (!StringUtils.isEmpty(this.sort)) {
            String[] sorts = this.sort.split(",");
            if (sorts.length > 0) {
                Sort sort = new Sort();
                sort.setProp(sorts[0]);
                if (sorts.length > 1) {
                    sort.setDescending(sorts[1].equals("descending"));
                }

                return sort;
            }
        }

        return null;
    }

    public static class Sort implements Serializable {
        private static final long serialVersionUID = 1L;
        private String prop;
        private String column;
        private Boolean isDescending;

        public Sort() {
        }

        public String getProp() {
            return this.prop;
        }

        public void setProp(String prop) {
            this.prop = prop;
        }

        public String getColumn() {
            return this.column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public Boolean getDescending() {
            return this.isDescending;
        }

        public Boolean isDescending() {
            return this.isDescending;
        }

        public void setDescending(Boolean descending) {
            this.isDescending = descending;
        }
    }
}
