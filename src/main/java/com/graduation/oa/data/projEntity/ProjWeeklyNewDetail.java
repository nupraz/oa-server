package com.graduation.oa.data.projEntity;

public class ProjWeeklyNewDetail {

    //序号
    private String workNum;
    //工作主题
    private String workTheme;
    //参与人
    private String workPartner;
    //工作内容
    private String workContent;
    //完成情况
    private String workSchedule;

    public String getWorkNum() {
        return workNum;
    }

    public void setWorkNum(String workNum) {
        this.workNum = workNum;
    }

    public String getWorkTheme() {
        return workTheme;
    }

    public void setWorkTheme(String workTheme) {
        this.workTheme = workTheme;
    }

    public String getWorkPartner() {
        return workPartner;
    }

    public void setWorkPartner(String workPartner) {
        this.workPartner = workPartner;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }

    @Override
    public String toString() {
        return "ScheduleList{" +
                "workNum='" + workNum + '\'' +
                ", workTheme='" + workTheme + '\'' +
                ", workPartner='" + workPartner + '\'' +
                ", workContent='" + workContent + '\'' +
                ", workSchedule='" + workSchedule + '\'' +
                '}';
    }

    public ProjWeeklyNewDetail() {
    }

    public ProjWeeklyNewDetail(String workNum, String workTheme, String workPartner, String workContent, String workSchedule) {
        this.workNum = workNum;
        this.workTheme = workTheme;
        this.workPartner = workPartner;
        this.workContent = workContent;
        this.workSchedule = workSchedule;
    }
}
