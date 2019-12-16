package com.graduation.oa.data.empOrderEntity;

import com.graduation.oa.common.support.CacheUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rest_menu_info")

public class RestMenuInfo implements Serializable {
    @Id
    private String menuId;
    private String restId;
    private String menuName;
    private String menuPrice;
    private String inUse;
    private Date manageTime;
    private String manageUser;
    @Transient
    private String inUseName;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getInUse() {
        return inUse;
    }

    public void setInUse(String inUse) {
        this.inUse = inUse;
    }

    public Date getManageTime() {
        return manageTime;
    }

    public void setManageTime(Date manageTime) {
        this.manageTime = manageTime;
    }

    public String getManageUser() {
        return manageUser;
    }

    public void setManageUser(String manageUser) {
        this.manageUser = manageUser;
    }

    public String getInUseName() {
        if (StringUtils.isEmpty(this.inUseName) && !StringUtils.isEmpty(this.inUse)) {
            this.inUseName = CacheUtils.transDict("yesOrNo", this.inUse);
        }
        return inUseName;
    }
}
