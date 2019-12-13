package com.graduation.oa.data.empOrderEntity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "start_order_info")

public class StartOrderInfo implements Serializable {
    @Id
    private String sysGuid;
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private Date manageTime;
    private String manageUser;
    @Transient
    private String startDateTemp;
    @Transient
    private String endDateTemp;

    public String getStartDateTemp() {
        return startDateTemp;
    }

    public void setStartDateTemp(String startDateTemp) {
        this.startDateTemp = startDateTemp;
    }

    public String getEndDateTemp() {
        return endDateTemp;
    }

    public void setEndDateTemp(String endDateTemp) {
        this.endDateTemp = endDateTemp;
    }

    public String getSysGuid() {
        return sysGuid;
    }

    public void setSysGuid(String sysGuid) {
        this.sysGuid = sysGuid;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

}
