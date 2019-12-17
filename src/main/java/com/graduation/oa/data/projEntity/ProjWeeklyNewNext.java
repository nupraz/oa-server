package com.graduation.oa.data.projEntity;

import java.io.Serializable;

public class ProjWeeklyNewNext implements Serializable {

    //序号
    private String num;
    //主题
    private String theme;
    //参与人
    private String partner;
    //明细
    private String detail;
    //进度
    private String schedule;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
