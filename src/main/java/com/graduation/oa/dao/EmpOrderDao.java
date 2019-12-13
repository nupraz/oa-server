package com.graduation.oa.dao;

import com.graduation.oa.data.empOrderEntity.EmpOrderInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface EmpOrderDao extends Mapper<EmpOrderInfo> {

    /**
     * 订餐查询
     * @return
     */
    List<EmpOrderInfo> fetchEmpOrders(@Param("fuzzy") String fuzzy, @Param("empName") String empName,
                                      @Param("startDate") String startDate, @Param("endDate") String endDate,
                                      @Param("column") String column, @Param("sort") String sort, @Param("empId") String empId,
                                      @Param("roles") List<Integer> roles);

    /**
     * 导出订餐统计
     * @return
     */
    List<EmpOrderInfo> fetchEmpOrdersStatistics(@Param("fuzzy") String fuzzy, @Param("empName") String empName,
                                                @Param("startDate") String startDate, @Param("endDate") String endDate);

    int isInOrderTime(Date date);

    int isAlreadyOrder(@Param("empId") String empId, @Param("startOrderTime") String startOrderTime, @Param("endOrderTime") String endOrderTime);

    List<EmpOrderInfo> selectStartAndEndOrdertime(Date date);
}
