package com.graduation.oa.data.projEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "proj_weekly")
public class ProjWeeklyNew implements Serializable {
    @Id
    private String pwId;
    private String projId;
    private String projName;
    private String startDate;
    private String endDate;
    private String projManage;
    private String empName;
    private String contentDetail;
    private String manageUser;
    private Date manageTime;
    private String pwState;

    @Transient
    private String dateWeek;

    public String getPwId() {
        return pwId;
    }

    public void setPwId(String pwId) {
        this.pwId = pwId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getDateWeek() {
        return dateWeek;
    }

    public void setDateWeek(String dateWeek) {
        this.dateWeek = dateWeek;
    }

    public String getProjManage() {
        return projManage;
    }

    public void setProjManage(String projManage) {
        this.projManage = projManage;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getContentDetail() {
        return contentDetail;
    }

    public void setContentDetail(String contentDetail) {
        this.contentDetail = contentDetail;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPwState() {return pwState; }

    public void setPwState(String pwState) { this.pwState = pwState; }

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

    public ProjWeeklyNew(String pwId, String projId, String projName, String dateWeek, String projManage, String empName, String contentDetail, String manageUser, Date manageTime, String pwState, String startDate, String endDate) {
        this.pwId = pwId;
        this.projId = projId;
        this.projName = projName;
        this.dateWeek = dateWeek;
        this.projManage = projManage;
        this.empName = empName;
        this.contentDetail = contentDetail;
        this.manageUser = manageUser;
        this.manageTime = manageTime;
        this.pwState = pwState;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "ProjWeeklyNew{" +
                "pwId='" + pwId + '\'' +
                ", projId='" + projId + '\'' +
                ", projName='" + projName + '\'' +
                ", dateWeek='" + dateWeek + '\'' +
                ", projManage='" + projManage + '\'' +
                ", empName='" + empName + '\'' +
                ", contentDetail='" + contentDetail + '\'' +
                ", manageUser='" + manageUser + '\'' +
                ", manageTime=" + manageTime +
                ", pwState='" + pwState + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public ProjWeeklyNew() {
    }
}
