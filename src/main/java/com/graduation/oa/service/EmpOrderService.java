package com.graduation.oa.service;

import com.graduation.oa.data.empOrderEntity.EmpOrderInfo;
import com.graduation.oa.data.empOrderEntity.RestMenuInfo;
import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EmpOrderService {
    //查询订餐信息
    PageInfo<EmpOrderInfo> fetchEmpOrders(String fuzzy, String empName, String startDate,
                                          String endDate, String sort, Integer page,
                                          Integer limit, String empId, String roleIds);
    //新增员工订餐
    Map<String,Object> createOrderInfo(EmpOrderInfo empOrderInfo, String empId);
    //修改员工订餐
    Map<String,Object> modifyOrderInfo(EmpOrderInfo empOrderInfo, String empName);
    //删除订餐信息
    Map<String,Object> deleteEmpOrderInfo(String ids, String name);
    //导出订餐信息查询
    List<EmpOrderInfo> exportEmpOrderData(Map<String, String> param, String empId, String roleIds);
    //导出订餐汇总信息查询
    List<EmpOrderInfo> exportEmpOrderStatistics(Map<String, String> param);
    //是否显示添加按钮
    Map<String,Object> fetchIsShowAddAndEmpOptions(String empId, String roleIds);
    //获取员工信息
    Map<String, List> getEmpData();
    //获取所有可用餐厅信息
    List<RestaurantInfo> getRestaurantsInUse();
    //通过餐厅id 拉取可用菜单
    List<RestMenuInfo> getMenusInUseByRestId(String restId);

}
