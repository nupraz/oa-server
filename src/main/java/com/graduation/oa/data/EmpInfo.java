package com.graduation.oa.data;

import com.bestvike.commons.utils.StringUtils;
import com.graduation.oa.common.support.CacheUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class EmpInfo extends BasePageData implements Serializable {
    private  static  final long serialVersionUID = 1L;

    @Id
    private String id;



    private String empId;
    private String EmpName;
    private String deptId;
    private String grade;
    private String postCode;
    private String status;
    private String needScore;
    private Date manageTime;
    private String manageUser;
    private String remark;

    @Transient
    private String deptName;
    @Transient
    private String postName;

    /**
     * 是否需要员工评价的状态
     * 99 可以参与评价
     * 66 已完成上月评价
     * 11 部门评价尚未完成 不能进行员工评价
     * 22 参与评价 且有暂存数据
     */
    @Transient
    private String state;
    @Transient
    private BigDecimal empScore;
    @Transient
    private String assessRemark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNeedScore() {
        return needScore;
    }

    public void setNeedScore(String needScore) {
        this.needScore = needScore;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getEmpScore() {
        return empScore;
    }

    public void setEmpScore(BigDecimal empScore) {
        this.empScore = empScore;
    }

    public String getAssessRemark() {
        return assessRemark;
    }

    public void setAssessRemark(String assessRemark) {
        this.assessRemark = assessRemark;
    }
}
