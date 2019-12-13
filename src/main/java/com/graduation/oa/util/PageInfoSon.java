package com.graduation.oa.util;

import com.bestvike.commons.utils.StringUtils;
import com.graduation.oa.data.empOrderEntity.EmpOrderInfo;
import com.graduation.oa.util.CommentAnno;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PageInfoSon<T> extends PageInfo<T> {
    @CommentAnno("补助金额统计")
    private double subsidySum;
    @CommentAnno("差旅工作量 售前工作量")
    private double numSum;
    @CommentAnno("差旅出差次数")
    private long totalSize;

    @CommentAnno("出差")
    private long count1;
    @CommentAnno("请假")
    private long count2;
    @CommentAnno("缺卡")
    private long count3;
    @CommentAnno("迟到/早退")
    private long count4;
    @CommentAnno("调休")
    private long count5;
    @CommentAnno("外出")
    private long count6;
    @CommentAnno("加班次数")
    private long count7;
    @CommentAnno("加班时间")
    private long count8;

    @CommentAnno("订餐费用sum")
    private long priceSum;

    @CommentAnno("订餐总数")
    private long orderCount;

    @CommentAnno("总不加班工作量")
    private String allWorkLoad;
    @CommentAnno("总加班工作量")
    private String allAddWorkLoad;
    @CommentAnno("总工作量")
    private String allSum;

    public String getAllWorkLoad() {
        return allWorkLoad;
    }

    public void setAllWorkLoad(String allWorkLoad) {
        this.allWorkLoad = allWorkLoad;
    }

    public String getAllAddWorkLoad() {
        return allAddWorkLoad;
    }

    public void setAllAddWorkLoad(String allAddWorkLoad) {
        this.allAddWorkLoad = allAddWorkLoad;
    }

    public String getAllSum() {
        return allSum;
    }

    public void setAllSum(String allSum) {
        this.allSum = allSum;
    }

    public long getOrderCount() { return orderCount; }

    public void setOrderCount(long orderCount) { this.orderCount = orderCount; }

    public long getPriceSum() { return priceSum; }

    public void setPriceSum(long priceSum) { this.priceSum = priceSum; }

    public double getNumSum() { return numSum; }

    public void setNumSum(double numSum) {
        this.numSum = numSum;
    }

    public double getSubsidySum() { return subsidySum; }

    public void setSubsidySum(double subsidySum) { this.subsidySum = subsidySum; }


    public static <T> PageInfoSon<T> convertPageInfo2PageSon(PageInfo<T> pageInfo, PageInfo<T> pageInfoAll) {

        PageInfoSon<T> tpageInfoSon = new PageInfoSon<>();
        tpageInfoSon.setEndRow(pageInfo.getEndRow());
        tpageInfoSon.setHasNextPage(pageInfo.isHasNextPage());
        tpageInfoSon.setHasPreviousPage(pageInfo.isHasPreviousPage());
        tpageInfoSon.setIsFirstPage(pageInfo.isIsFirstPage());
        tpageInfoSon.setIsLastPage(pageInfo.isIsLastPage());
        tpageInfoSon.setList(pageInfo.getList());
        tpageInfoSon.setNavigateFirstPage(pageInfo.getNavigateFirstPage());
        tpageInfoSon.setNavigateLastPage(pageInfo.getNavigateLastPage());
        tpageInfoSon.setNavigatepageNums(pageInfo.getNavigatepageNums());
        tpageInfoSon.setNavigatePages(pageInfo.getNavigatePages());
        tpageInfoSon.setNextPage(pageInfo.getNextPage());
        tpageInfoSon.setPageNum(pageInfo.getPageNum());
        tpageInfoSon.setPages(pageInfo.getPages());
        tpageInfoSon.setPageSize(pageInfo.getPageSize());
        tpageInfoSon.setPrePage(pageInfo.getPrePage());
        tpageInfoSon.setSize(pageInfo.getSize());
        tpageInfoSon.setStartRow(pageInfo.getStartRow());
        tpageInfoSon.setTotal(pageInfo.getTotal());
        if (!(tpageInfoSon.getList() != null && tpageInfoSon.getList().size() > 0)){
            return tpageInfoSon;
        }
        T t = tpageInfoSon.getList().get(0);
        List<T> listPageInfo = pageInfoAll.getList();
        /*if (t instanceof PresaleSupportInfo) {
            //如果是售前信息
            instancePresalesupportInfo(tpageInfoSon, listPageInfo);
        }
        if (t instanceof EmpTravel) {
            //如果是
            instanceEmpTravel(tpageInfoSon, listPageInfo);
        }
        if (t instanceof EmpAttend) {
            instanceEmpAttend(tpageInfoSon,listPageInfo);
        }*/
        if (t instanceof EmpOrderInfo) {
            instanceEmpOrderInfo(tpageInfoSon,listPageInfo);
        }
        /*if (t instanceof EmpDailyDTO){
            instanceEmpDailyDTO(tpageInfoSon,listPageInfo);
        }*/

        return tpageInfoSon;
    }

    /*private static <T> void instanceEmpDailyDTO(PageInfoSon<T> tpageInfoSon, List<T> listPageInfo) {
        if (listPageInfo != null && listPageInfo.size() > 0) {
            BigDecimal workLoadTemp = new BigDecimal(0.00);
            BigDecimal addWorkLoadTemp = new BigDecimal(0.00);
            for (T t : listPageInfo) {
                EmpDailyDTO empDailyDTO = (EmpDailyDTO) t;
                String workLoad = empDailyDTO.getWorkLoad();
                if (workLoad != null && !"".equals(workLoad)) {
                    BigDecimal v = new BigDecimal(0.00);
                    try {
                        v = new BigDecimal(workLoad);
                    } catch (NumberFormatException e) {
                        System.out.println("工作量数据有问题！");
                    }
                    workLoadTemp = workLoadTemp.add(v);
                }
                String addWorkLoad = empDailyDTO.getAddWorkLoad();
                if (addWorkLoad != null && !"".equals(addWorkLoad)) {
                    BigDecimal v = new BigDecimal(0.00);
                    try {
                        v = new BigDecimal(addWorkLoad);
                    }catch (NumberFormatException e){
                        System.out.println("加班工作量数据有问题！");
                    }
                    addWorkLoadTemp = addWorkLoadTemp.add(v);
                }
            }
            BigDecimal allWorkLoadTemp = workLoadTemp.add(addWorkLoadTemp);
            tpageInfoSon.setAllAddWorkLoad(String.valueOf(addWorkLoadTemp));
            tpageInfoSon.setAllWorkLoad(String.valueOf(workLoadTemp));
            tpageInfoSon.setAllSum(String.valueOf(allWorkLoadTemp));
        }

    }*/

    private static <T> void instanceEmpOrderInfo(PageInfoSon<T> tpageInfoSon, List<T> list) {
        if (list != null && list.size() > 0) {
            tpageInfoSon.setOrderCount(list.size());

            if (tpageInfoSon.getPriceSum() == 0) tpageInfoSon.setPriceSum(0);

            if (list.get(0) instanceof EmpOrderInfo) {
                Integer priceSum = 0;
                for (T t : list) {
                    EmpOrderInfo t1 = (EmpOrderInfo) t;
                    try {
                        priceSum = priceSum + Integer.parseInt(t1.getPrice());
                    } catch (Exception e) {
                        continue;
                    }
                    if (priceSum == null) priceSum = 0;
                    tpageInfoSon.setPriceSum(priceSum);
                }
            }
        }
    }

    /*private static <T> void instancePresalesupportInfo(PageInfoSon<T> tpageInfoSon, List<T> list) {

        if (tpageInfoSon.getNumSum() == 0) tpageInfoSon.setNumSum(0);
        if (list != null && list.size() > 0) {
            if (list.get(0) instanceof PresaleSupportInfo) {
                for (T t : list) {
                    PresaleSupportInfo t1 = (PresaleSupportInfo) t;
                    double numSum = tpageInfoSon.getNumSum();
                    Integer workLoad = t1.getWorkLoad();
                    if (workLoad == null) workLoad = 0;
                    tpageInfoSon.setNumSum(numSum + workLoad);
                }
            }
        }
    }*/

    /*private static <T> void instanceEmpTravel(PageInfoSon<T> tpageInfoSon, List<T> list) {
        if (list != null && list.size() > 0) {
            tpageInfoSon.setTotalSize(list.size());

            if (tpageInfoSon.getNumSum() == 0) tpageInfoSon.setNumSum(0);
            if (tpageInfoSon.getSubsidySum() == 0) tpageInfoSon.setSubsidySum(0);

            if (list.get(0) instanceof EmpTravel) {
                for (T t : list) {
                    EmpTravel t1 = (EmpTravel) t;
                    double numSum = tpageInfoSon.getNumSum();
                    double subsidyTotal = tpageInfoSon.getSubsidySum();
                    Double workLoad = 0.0;
                    Double subsidy = 0.0;
                    try {
                        workLoad = Double.parseDouble(t1.getDays());
                        subsidy = t1.getTotalAmount().doubleValue();
                    } catch (Exception e) {
                        continue;
                    }
                    if (workLoad == null) workLoad = 0.0;
                    tpageInfoSon.setNumSum(numSum + workLoad);
                    tpageInfoSon.setSubsidySum(subsidyTotal + subsidy);
                }
            }
        }
    }*/

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCount1() {
        return count1;
    }

    public void setCount1(long count1) {
        this.count1 = count1;
    }

    public long getCount2() {
        return count2;
    }

    public void setCount2(long count2) {
        this.count2 = count2;
    }

    public long getCount3() {
        return count3;
    }

    public void setCount3(long count3) {
        this.count3 = count3;
    }

    public long getCount4() {
        return count4;
    }

    public void setCount4(long count4) {
        this.count4 = count4;
    }

    public long getCount5() {
        return count5;
    }

    public void setCount5(long count5) {
        this.count5 = count5;
    }

    public long getCount6() {
        return count6;
    }

    public void setCount6(long count6) {
        this.count6 = count6;
    }

    public long getCount7() {
        return count7;
    }

    public void setCount7(long count7) {
        this.count7 = count7;
    }

    public long getCount8() {
        return count8;
    }

    public void setCount8(long count8) {
        this.count8 = count8;
    }
    /*private static <T> void instanceEmpAttend(PageInfoSon<T> tpageInfoSon, List<T> list) {
        long count1 = 0;
//        @CommentAnno("请假")
        long count2 = 0;
//        @("缺卡")
        long count3 = 0;
//        @CommentAnno("迟到/早退")
        long count4 = 0;
//        @CommentAnno("调休")
        long count5 = 0;
//        @CommentAnno("外出")
        long count6 = 0;
//        @CommentAnno("加班次数")
        long count7 = 0;
//        @CommentAnno("加班时间")
        long count8 = 0;


        if (tpageInfoSon.getNumSum() == 0) tpageInfoSon.setNumSum(0);
        if (list != null && list.size() > 0) {
            if (list.get(0) instanceof EmpAttend) {
                List<EmpAttend> collect = list.stream().map(s -> (EmpAttend) s).collect(Collectors.toList());
                for (EmpAttend empAttend : collect) {
                    String onDuty = empAttend.getOnDuty();
                    if (!StringUtils.isEmpty(onDuty)) {
                        if (onDuty.endsWith("出差")) {
                            count1++;
                        } else if (onDuty.endsWith("请假")) {
                            count2++;
                        } else if (onDuty.endsWith("缺卡")) {
                            count3++;
                        } else if (onDuty.endsWith("调休")) {
                            count5++;
                        } else if (onDuty.endsWith("外出")) {
                            count6++;
                        }
                    }
                    Integer earlyMinute = empAttend.getEarlyMinute();
                    Integer lateMinute = empAttend.getLateMinute();
                    if ((earlyMinute!=null&&lateMinute!=null)&&(earlyMinute > 0 || lateMinute > 0)) {
                        count4++;
                    }
                    Integer overtimeMinute = empAttend.getOvertimeMinute();

                    if (overtimeMinute!=null&&overtimeMinute > 0) {
                        count7++;
                        count8+=overtimeMinute;
                    }
                    tpageInfoSon.setCount1(count1);
                    tpageInfoSon.setCount2(count2);
                    tpageInfoSon.setCount3(count3);
                    tpageInfoSon.setCount4(count4);
                    tpageInfoSon.setCount5(count5);
                    tpageInfoSon.setCount6(count6);
                    tpageInfoSon.setCount7(count7);
                    tpageInfoSon.setCount8(count8);

                }

            }
        }

    }*/
}