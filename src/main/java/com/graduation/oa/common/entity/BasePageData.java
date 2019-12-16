package com.graduation.oa.common.entity;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Transient;
import java.io.Serializable;
@NameStyle(Style.camelhump)
public class BasePageData extends BaseData implements Serializable {
    private static final long serialVersionUID = 1L;
    @Transient
    protected int page;
    @Transient
    protected int limit;

    public BasePageData() {
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
