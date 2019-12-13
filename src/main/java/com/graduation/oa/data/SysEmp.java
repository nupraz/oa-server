package com.graduation.oa.data;

import com.graduation.oa.support.CacheUtils;
import com.github.pagehelper.util.StringUtil;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "emp_info")
public class SysEmp implements Serializable {

    @Id
    private String empId;
    private String id;
    private String empName;
    private String userId;
    private String empState;//0000:已开户状态 1111：新增未开户  9999：销户状态
    private String univId;
    private String graduationYear;
    private String learnProfessional;
    private String relevantType;
    private String politicalStatus;
    private String address;
    private String domicile;
    private String employedDate;
    private String registeredDate;
    private String positiveDate;
    private String departureDate;
    private String certNo;
    private String postId;
    private String postName;
    private String phoneNo;
    private String email;
    private String qq;
    private String urgContactPerson;
    private String urgContactPhone;
    private String urgContactRelation;
    private Date manageTime;
    private String manageUser;
    private String remark;
    private String areaId;
    private String supervisorId;
    private String uniyLevel;
    private String entrySalary;
    private String recruitsSource;
    private String bankAccount;
    private Integer age;
    private String category;
    private String foreignLangu;
    private String educational;
    private String birthday;
    private String sex;
    private String specialSkill;
    private String specialProj;

    private String estimateRegistered;
    private String domicileDetailed;
    private String addressDetailed;
    private String employeeState;

    private String extension;

    @Transient
    private String deptId;
    @Transient
    private String  contractBeginDate;
    @Transient
    private String contractEndDate;
    @Transient
    private String contractTypes;
    @Transient
    private List<String> oldDeptIds;


    @Transient
    private String empStateName;
    @Transient
    private String uniyLevelName;
    @Transient
    private String politicalStatusName;
    @Transient
    private String relevantTypeName;

    @Transient
    private String educationalName;
    @Transient
    private String foreignLanguName;
    @Transient
    private String recruitsSourceName;
    @Transient
    private String categoryName;
    @Transient
    private String univName;
    @Transient
    private String learnProfessionalName;

    @Transient
    private String postIdAndName;
    @Transient
    private String deptName;
    @Transient
    private String workYear;
    @Transient
    private String preWorkYear;
    @Transient
    private String ageAllowance;

    public String getEmployeeState() {
        return employeeState;
    }

    public void setEmployeeState(String employeeState) {
        this.employeeState = employeeState;
    }

    public String getPostIdAndName() {
        if(!StringUtil.isEmpty(this.postId)){
            postIdAndName = this.postId + "_" + CacheUtils.transDict("empPosition",this.postId);
        }
        return postIdAndName;
    }

    public String getLearnProfessionalName() {
        if(!StringUtil.isEmpty(this.learnProfessional)){
            learnProfessionalName = CacheUtils.transDict("learnProfessional",this.learnProfessional);
        }
        return learnProfessionalName;
    }

    public String getUnivName() {
        return univName;
    }

    public String getCategoryName() {
        if(!StringUtil.isEmpty(this.category)){
            categoryName = CacheUtils.transDict("category",this.category);
        }
        return categoryName;
    }

    public String getEducationalName() {
        if(!StringUtil.isEmpty(this.educational)){
            educationalName = CacheUtils.transDict("educational",this.educational);
        }
        return educationalName;
    }

    public String getForeignLanguName() {
        if(!StringUtil.isEmpty(this.educational)){
            foreignLanguName = CacheUtils.transDict("foreignLangu",this.foreignLangu);
        }
        return foreignLanguName;
    }

    public String getRecruitsSourceName() {
        if(!StringUtil.isEmpty(this.educational)){
            recruitsSourceName = CacheUtils.transDict("recruitmentSources",this.recruitsSource);
        }
        return recruitsSourceName;
    }

    public String getEmpStateName() {
        if(!StringUtil.isEmpty(this.employeeState)){
            empStateName = CacheUtils.transDict("employeeState",this.employeeState);
        }
        return empStateName;
    }

    public String getPoliticalStatusName() {
        if (!StringUtil.isEmpty(this.politicalStatus)) {
            politicalStatusName = CacheUtils.transDict("politicalStatus",this.politicalStatus);
        }
        return politicalStatusName;
    }

    public String getUniyLevelName() {
        if(!StringUtil.isEmpty(this.uniyLevel)){
            uniyLevelName = CacheUtils.transDict("collegeLevel",this.uniyLevel);
        }
        return uniyLevelName;
    }

    public String getRelevantTypeName() {
        if(!StringUtil.isEmpty(this.relevantType)){
            relevantTypeName = CacheUtils.transDict("yesOrNo",this.relevantType);
        }
        return relevantTypeName;
    }

    public List<String> getOldDeptIds() {
        return oldDeptIds;
    }


    public void setOldDeptIds(List<String> oldDeptIds) {
        this.oldDeptIds = oldDeptIds;
    }

    @Transient
    private String deptIdsResult;
    @Transient
    private List<String> deptIds;

    public String getUniyLevel() {
        return uniyLevel;
    }


    public void setUniyLevel(String uniyLevel) {
        this.uniyLevel = uniyLevel;
    }


    public String getEntrySalary() {
        return entrySalary;
    }


    public void setEntrySalary(String entrySalary) {
        this.entrySalary = entrySalary;
    }


    public String getRecruitsSource() {
        return recruitsSource;
    }


    public void setRecruitsSource(String recruitsSource) {
        this.recruitsSource = recruitsSource;
    }


    public String getAreaId() {
        return areaId;
    }


    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }


    public String getEmpId() {
        return empId;
    }


    public void setEmpId(String empId) {
        this.empId = empId;
    }


    public String getEmpName() {
        return empName;
    }


    public void setEmpName(String empName) {
        this.empName = empName;
    }


    public String getEmpState() { return empState; }


    public void setEmpState(String empState) {
        this.empState = empState;
    }


    public String getUnivId() {
        return univId;
    }


    public void setUnivId(String univId) {
        this.univId = univId;
    }


    public String getGraduationYear() {
        return graduationYear;
    }


    public void setGraduationYear(String graduationYear) {
        this.graduationYear = graduationYear;
    }


    public String getLearnProfessional() {
        return learnProfessional;
    }


    public void setLearnProfessional(String learnProfessional) {
        this.learnProfessional = learnProfessional;
    }


    public String getRelevantType() { return relevantType; }


    public void setRelevantType(String relevantType) {
        this.relevantType = relevantType;
    }


    public String getPoliticalStatus() { return politicalStatus; }


    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getDomicile() {
        return domicile;
    }


    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }


    public String getEmployedDate() {
        return employedDate;
    }


    public void setEmployedDate(String employedDate) {
        this.employedDate = employedDate;
    }


    public String getRegisteredDate() {
        return registeredDate;
    }


    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }


    public String getPositiveDate() {
        return positiveDate;
    }


    public void setPositiveDate(String positiveDate) {
        this.positiveDate = positiveDate;
    }


    public String getDepartureDate() {
        return departureDate;
    }


    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }


    public String getCertNo() {
        return certNo;
    }


    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }


    public String getPostId() { return postId; }


    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getPhoneNo() {
        return phoneNo;
    }


    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getQq() {
        return qq;
    }


    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getUrgContactPerson() {
        return urgContactPerson;
    }


    public void setUrgContactPerson(String urgContactPerson) {
        this.urgContactPerson = urgContactPerson;
    }


    public String getUrgContactPhone() {
        return urgContactPhone;
    }


    public void setUrgContactPhone(String urgContactPhone) {
        this.urgContactPhone = urgContactPhone;
    }


    public String getUrgContactRelation() {
        return urgContactRelation;
    }


    public void setUrgContactRelation(String urgContactRelation) {
        this.urgContactRelation = urgContactRelation;
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


    public List<String> getDeptIds() { return deptIds; }


    public void setDeptIds(List<String> deptIds) { this.deptIds = deptIds; }


    public String getDeptIdsResult() {
        return deptIdsResult;
    }


    public void setDeptIdsResult(String deptIdsResult) {
        this.deptIdsResult = deptIdsResult;
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getSupervisorId() {
        return supervisorId;
    }


    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }


    public String getPostName() {
        return postName;
    }


    public void setPostName(String postName) {
        this.postName = postName;
    }


    public Integer getAge() {
        return age;
    }


    public void setAge(Integer age) {
        this.age = age;
    }


    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getForeignLangu() {
        return foreignLangu;
    }


    public void setForeignLangu(String foreignLangu) {
        this.foreignLangu = foreignLangu;
    }


    public String getEducational() {
        return educational;
    }


    public void setEducational(String educational) {
        this.educational = educational;
    }


    public String getBirthday() { return birthday; }


    public void setBirthday(String birthday) { this.birthday = birthday;
    }


    public String getSex() {
        return sex;
    }


    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getSpecialSkill() {
        return specialSkill;
    }


    public void setSpecialSkill(String specialSkill) {
        this.specialSkill = specialSkill;
    }


    public String getSpecialProj() {
        return specialProj;
    }


    public void setSpecialProj(String specialProj) {
        this.specialProj = specialProj;
    }


    public String getDeptId() {
        return deptId;
    }

    public String getEstimateRegistered() {
        return estimateRegistered;
    }

    public void setEstimateRegistered(String estimateRegistered) {
        this.estimateRegistered = estimateRegistered;
    }

    public String getDomicileDetailed() {
        return domicileDetailed;
    }

    public void setDomicileDetailed(String domicileDetailed) {
        this.domicileDetailed = domicileDetailed;
    }

    public String getAddressDetailed() {
        return addressDetailed;
    }

    public void setAddressDetailed(String addressDetailed) {
        this.addressDetailed = addressDetailed;
    }


    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }


    @Override
    public String toString() {
        return "SysEmp{" +
                "empId='" + empId + '\'' +
                ", empName='" + empName + '\'' +
                ", userId='" + userId + '\'' +
                ", empState='" + empState + '\'' +
                ", univId='" + univId + '\'' +
                ", graduationYear='" + graduationYear + '\'' +
                ", learnProfessional='" + learnProfessional + '\'' +
                ", relevantType='" + relevantType + '\'' +
                ", politicalStatus='" + politicalStatus + '\'' +
                ", address='" + address + '\'' +
                ", domicile='" + domicile + '\'' +
                ", employedDate='" + employedDate + '\'' +
                ", registeredDate='" + registeredDate + '\'' +
                ", positiveDate='" + positiveDate + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", certNo='" + certNo + '\'' +
                ", postId='" + postId + '\'' +
                ", postName='" + postName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                ", qq='" + qq + '\'' +
                ", urgContactPerson='" + urgContactPerson + '\'' +
                ", urgContactPhone='" + urgContactPhone + '\'' +
                ", urgContactRelation='" + urgContactRelation + '\'' +
                ", manageTime=" + manageTime +
                ", manageUser='" + manageUser + '\'' +
                ", remark='" + remark + '\'' +
                ", areaId='" + areaId + '\'' +
                ", supervisorId='" + supervisorId + '\'' +
                ", uniyLevel='" + uniyLevel + '\'' +
                ", entrySalary='" + entrySalary + '\'' +
                ", recruitsSource='" + recruitsSource + '\'' +
                ", age=" + age +
                ", category='" + category + '\'' +
                ", foreignLangu='" + foreignLangu + '\'' +
                ", educational='" + educational + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex='" + sex + '\'' +
                ", specialSkill='" + specialSkill + '\'' +
                ", specialProj='" + specialProj + '\'' +
                ", deptId='" + deptId + '\'' +
                ", contractBeginDate='" + contractBeginDate + '\'' +
                ", contractEndDate='" + contractEndDate + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", contractTypes='" + contractTypes + '\'' +
                ", oldDeptIds=" + oldDeptIds +
                ", deptIdsResult='" + deptIdsResult + '\'' +
                ", deptIds=" + deptIds +
                '}';
    }


    public String getContractTypes() {
        return contractTypes;
    }


    public void setContractTypes(String contractTypes) {
        this.contractTypes = contractTypes;
    }


    public String getContractBeginDate() {
        return contractBeginDate;
    }


    public void setContractBeginDate(String contractBeginDate) {
        this.contractBeginDate = contractBeginDate;
    }


    public String getContractEndDate() {
        return contractEndDate;
    }


    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    public String getPreWorkYear() {
        return preWorkYear;
    }

    public void setPreWorkYear(String preWorkYear) {
        this.preWorkYear = preWorkYear;
    }

    public String getAgeAllowance() {
        if(!StringUtil.isEmpty(this.workYear)){
            ageAllowance = CacheUtils.transDict("ageAllowance",this.workYear);
        }
        return ageAllowance;
    }

    public void setAgeAllowance(String ageAllowance) {
        this.ageAllowance = ageAllowance;
    }
}
