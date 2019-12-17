package com.graduation.oa.data.projEntity;
import java.io.Serializable;

public class ProjWeeklyNewRisk implements Serializable {

    //序号
    private String num;
    //风险概述
    private String riskConcept;
    //负责人
    private String principal;
    //需解决和协调的事宜、问题	解决期限
    private String issue;
    //解决措施
    private String solution;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRiskConcept() {
        return riskConcept;
    }

    public void setRiskConcept(String riskConcept) {
        this.riskConcept = riskConcept;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public ProjWeeklyNewRisk(String num, String riskConcept, String principal, String issue, String solution) {
        this.num = num;
        this.riskConcept = riskConcept;
        this.principal = principal;
        this.issue = issue;
        this.solution = solution;
    }

    public ProjWeeklyNewRisk() {
    }
}
