package com.graduation.oa.service;

import com.graduation.oa.data.empOrderEntity.RestMenuInfo;
import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import com.graduation.oa.data.empOrderEntity.StartOrderInfo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface RestaurantService {
    //餐厅增删改查
    PageInfo<RestaurantInfo> getRestInfo(String fuzzy, String restName, String sort, Integer page, Integer limit);
    Map<String,Object> createRestInfo(RestaurantInfo restaurantInfo, String userName);
    Map<String,Object> modifyRestInfo(RestaurantInfo restaurantInfo, String userName);
    Map<String,Object> deleteRestInfo(String ids, String userName);

    //菜单增删改查
    PageInfo<RestMenuInfo> fetchMenuInfo(String fuzzy, String restId, String menuName, String sort, Integer page, Integer limit);
    Map<String,Object> createMenuInfo(RestMenuInfo restMenuInfo, String userName);
    Map<String,Object> modifyMenuInfo(RestMenuInfo restMenuInfo, String userName);
    Map<String,Object> deleteMenuInfo(String ids, String userName);

    //发起、取消订餐
    Map<String,Object> createOrderInfo(StartOrderInfo startOrderInfo, String userName);
    Map<String,Object> deleteOrderInfo(String userName);

    //查询是否正在订餐
    Map<String,Object> getIsHaveOrder();
    //获取所有预设订餐时间
    PageInfo<StartOrderInfo> getAllPresetTime(String fuzzy, Integer page, Integer limit);
    //删除所选预设订餐信息
    Map<String,Object> deletePresetTimeInfo(String ids, String name);
}
