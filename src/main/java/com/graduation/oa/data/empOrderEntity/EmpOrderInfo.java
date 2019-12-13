package com.graduation.oa.data.empOrderEntity;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "emp_order_info")
public class EmpOrderInfo implements Serializable {

    @Id
    private String sysGuid;
    private String empId;
    private String restId;
    private String menuId;
    private String price;
    private Date orderTime;
    private Date manageTime;
    private String manageUser;
    private String remark;

    @Transient
    private String empName;
    @Transient
    private String restName;
    @Transient
    private String menuName;
    @Transient
    private String historyFlag;
    @Transient
    private String totalMenu;
    @Transient
    private String priceSum;
    @Transient
    private String orderDate;
    @Transient
    private String startOrderTime;
    @Transient
    private String endOrderTime;

    public String getSysGuid() {
        return sysGuid;
    }

    public void setSysGuid(String sysGuid) {
        this.sysGuid = sysGuid;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getHistoryFlag() {
        return historyFlag;
    }

    public void setHistoryFlag(String historyFlag) {
        this.historyFlag = historyFlag;
    }

    public String getTotalMenu() {
        return totalMenu;
    }

    public void setTotalMenu(String totalMenu) {
        this.totalMenu = totalMenu;
    }

    public String getPriceSum() {
        return priceSum;
    }

    public void setPriceSum(String priceSum) {
        this.priceSum = priceSum;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStartOrderTime() {
        return startOrderTime;
    }

    public void setStartOrderTime(String startOrderTime) {
        this.startOrderTime = startOrderTime;
    }

    public String getEndOrderTime() {
        return endOrderTime;
    }

    public void setEndOrderTime(String endOrderTime) {
        this.endOrderTime = endOrderTime;
    }
}
