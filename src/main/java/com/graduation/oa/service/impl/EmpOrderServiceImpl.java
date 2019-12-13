package com.graduation.oa.service.impl;

import com.graduation.oa.dao.*;
import com.graduation.oa.data.EmpInfo;
import com.graduation.oa.data.SysEmp;
import com.graduation.oa.data.empOrderEntity.EmpOrderInfo;
import com.graduation.oa.data.empOrderEntity.RestMenuInfo;
import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.service.EmpOrderService;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class EmpOrderServiceImpl extends BaseService implements EmpOrderService {

    @Autowired
    private EmpOrderDao empOrderDao;
    @Autowired
    private StartOrderInfoDao startOrderInfoDao;
    @Autowired
    private RestMenuInfoDao restMenuInfoDao;
    @Autowired
    private EmpInfoDao empInfoDao;
    @Autowired
    private RestaurantInfoDao restaurantInfoDao;


    /**
     * @description 订餐查询
     * @author     zhengwenshan
     * @date        2019/7/30
     * @param       empName, startDate, endDate, sort, page, limit, empId, roleIds
     * @return      com.github.pagehelper.PageInfo<com.bestvike.oa.data.empOrderEntity.EmpOrderInfo>
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PageInfo<EmpOrderInfo> fetchEmpOrders(String fuzzy, String empName, String startDate,
                                                 String endDate, String sort, Integer page,
                                                 Integer limit, String empId, String roleIds) {
        PageInfo<EmpOrderInfo> empOrderPageInfo = PageHelper.startPage(page, limit).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                //初始默认那订餐时间降序
                String column = "orderTime";
                String sort1 = "descending";
                if(sort != null && !"".equals(sort)){
                    String[] arr = sort.split(",");
                    if(arr.length == 2){
                        sort1  = arr[1];
                        column = arr[0];
                    }
                }
                List<Integer> roles = new ArrayList<>();
//                List<EmpOrderInfo> list = null;
                //判断条件
                if (roleIds != null) {
                    //判断角色中是否有订餐管理员
                    if (roleIds.contains("8888")) {
                        //全部查询
                        roles.add(0);
                        empOrderDao.fetchEmpOrders(fuzzy, empName, startDate, endDate, column, sort1, empId,roles);
                    }else {
                        roles.add(1);
                        empOrderDao.fetchEmpOrders(fuzzy, empName, startDate, endDate, column, sort1,empId,roles);
                    }
                }else {
                    roles.add(2);
                    empOrderDao.fetchEmpOrders(fuzzy, empName, startDate, endDate, column, sort1,empId,roles);
                }
            }
        });
        return empOrderPageInfo;
    }

    /**
     * @description 新增员工订餐
     * @author     zhengwenshan
     * @date        2019/8/1
     * @param       empOrderInfo  empName
     * @return
    */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createOrderInfo(EmpOrderInfo empOrderInfo, String empId) {
        Map<String,Object> resultMap = new HashMap<>();
        if(empOrderInfo != null){
            empOrderInfo.setSysGuid(UUID.randomUUID().toString());
            if(empOrderInfo.getEmpId()==null || empOrderInfo.getEmpId()==""){
                empOrderInfo.setEmpId(empId);
            }
            empOrderInfo.setOrderTime(new Date());
            //获取正在订餐的起止时间
            List<EmpOrderInfo> listTime = empOrderDao.selectStartAndEndOrdertime(new Date());
            String startOrderTime = "";
            String endOrderTime = "";
            if(listTime != null && listTime.size() != 0){
                startOrderTime = listTime.get(0).getStartOrderTime();
                endOrderTime = listTime.get(0).getEndOrderTime();
            }
            //查询empIdz在该时间段是否已订餐
            if(empOrderDao.isAlreadyOrder(empOrderInfo.getEmpId(), startOrderTime, endOrderTime) > 0){
                resultMap.put("retCode","");
                resultMap.put("level","error");
                resultMap.put("retMsg","新增失败，该员工已订餐！");
                return resultMap;
            }
            //根据menuId查询menuPrice
            Example example = new Example(RestMenuInfo.class);
            example.createCriteria().andEqualTo("menuId",empOrderInfo.getMenuId());
            List<RestMenuInfo> list = restMenuInfoDao.selectByExample(example);
            if(list != null && list.size() > 0){
                empOrderInfo.setPrice(list.get(0).getMenuPrice());
            }
            empOrderDao.insert(empOrderInfo);
        }else {
            return null;
        }
        resultMap.put("retCode","00");
        resultMap.put("level","success");
        resultMap.put("retMsg","新增成功！");
        return resultMap;
    }

    /**
     * @description 修改员工订餐
     * @author     zhengwenshan
     * @date        2019/8/2
     * @param       empOrderInfo  empName
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> modifyOrderInfo(EmpOrderInfo empOrderInfo, String empName) {
        Map<String,Object> resultMap = new HashMap<>();
        if(empOrderInfo != null){
            empOrderInfo.setManageUser(empName);
            empOrderInfo.setManageTime(new Date());
            //根据menuId查询menuPrice
            Example example = new Example(RestMenuInfo.class);
            example.createCriteria().andEqualTo("menuId", empOrderInfo.getMenuId());
            List<RestMenuInfo> list = restMenuInfoDao.selectByExample(example);
            if(list != null && list.size() > 0){
                empOrderInfo.setPrice(list.get(0).getMenuPrice());
            }
            empOrderDao.updateByPrimaryKey(empOrderInfo);
        }else {
            return null;
        }
        resultMap.put("retCode","00");
        resultMap.put("level","success");
        resultMap.put("retMsg","修改成功");
        return resultMap;
    }

    /**
     * @description 删除订餐信息
     * @author     zhengwenshan
     * @date        2019/8/2
     * @param       ids name
     * @return
    */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> deleteEmpOrderInfo(String ids, String name) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<String> orderGuids = Arrays.asList(ids.split(","));
            for(String orderId: orderGuids){
                Example example = new Example(EmpOrderInfo.class);
                example.createCriteria().andEqualTo("sysGuid", orderId);
                empOrderDao.deleteByExample(example);
            }
            resultMap.put("retCode","00");
            resultMap.put("level","success");
            resultMap.put("retMsg","删除成功！");
        } catch (Exception e){
            resultMap.put("retCode","99");
            resultMap.put("level","error");
            resultMap.put("retMsg","删除失败！");
        }
        return resultMap;
    }
    /**
     * @description 导出订餐信息
     * @author     zhengwenshan
     * @date        2019/8/5
     * @param
     * @return
    */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<EmpOrderInfo> exportEmpOrderData(Map<String, String> param, String empId, String roleIds) {
        String empName = param.get("empName");
        String fuzzy = param.get("fuzzy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        //String empId = param.get("empId");
        String column = "";
        String sort1 = "";
        List<Integer> roles = new ArrayList<>();
        List<EmpOrderInfo> list = null;
        //判断条件
        if (roleIds != null) {
            //判断角色中是否有订餐管理员
            if (roleIds.contains("8888")) {
                //全部查询
                roles.add(0);
                list = empOrderDao.fetchEmpOrders(fuzzy, empName, startDate, endDate, column, sort1, empId,roles);
            }else {
                roles.add(1);
                list=empOrderDao.fetchEmpOrders(fuzzy, empName, startDate, endDate, column, sort1,empId,roles);
            }
        }else {
            roles.add(2);
            list=empOrderDao.fetchEmpOrders(fuzzy, empName, startDate, endDate, column, sort1,empId,roles);
        }
        return list;
    }
    /**
     * @description 导出订餐汇总信息
     * @author     zhengwenshan
     * @date        2019/8/7
     * @param
     * @return
    */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<EmpOrderInfo> exportEmpOrderStatistics(Map<String, String> param) {
        String empName = param.get("empName");
        String fuzzy = param.get("fuzzy");
        String startDate = param.get("startDate");
        String endDate = param.get("endDate");
        List<EmpOrderInfo> list = empOrderDao.fetchEmpOrdersStatistics(fuzzy, empName, startDate, endDate);
        return list;
    }
    /**
     * @description 按钮及下拉列表是否显示
     * @author     zhengwenshan
     * @date        2019/8/6
     * @param       empId roleIds
     * @return
    */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> fetchIsShowAddAndEmpOptions(String empId, String roleIds) {
        Map<String, Object> resultMap = new HashMap<>();
        Boolean isInOrderTime = false;
        Boolean isAlreadyOrder = false;
        Boolean isAdmin = false;
        List<EmpOrderInfo> listTime = null;
        //是否在订餐时间
        if(empOrderDao.isInOrderTime(new Date()) > 0){
            isInOrderTime = true;
            //获取正在订餐的起止时间
            listTime = empOrderDao.selectStartAndEndOrdertime(new Date());
        }
        String startOrderTime = "";
        String endOrderTime = "";
        if(listTime != null && listTime.size() != 0){
            startOrderTime = listTime.get(0).getStartOrderTime();
            endOrderTime = listTime.get(0).getEndOrderTime();
        }
        //在该订餐时间是否已订餐
        if(empOrderDao.isAlreadyOrder(empId, startOrderTime, endOrderTime) > 0){
            isAlreadyOrder = true;
        }
        //是否是订餐管理员
        if (roleIds.contains("8888")) {
            isAdmin = true;
        }
        resultMap.put("isInOrderTime",isInOrderTime);
        resultMap.put("isAlreadyOrder",isAlreadyOrder);
        resultMap.put("isAdmin",isAdmin);
        resultMap.put("startOrderTime",startOrderTime);
        resultMap.put("endOrderTime",endOrderTime);
        return resultMap;
    }
    /**
     * @description 获取员工信息
     * @author     zhengwenshan
     * @date        2019/8/6
     * @param
     * @return
    */
    @Override
    public Map<String, List> getEmpData() {
        Map<String,List> returnMap = new HashMap<>();
        Example example = new Example(SysEmp.class);
        example.createCriteria().andEqualTo("empState", "0000")
                .andEqualTo("employeeState", "0001")
                .orEqualTo("employeeState", "0000");
        example.orderBy("empId");
        List<EmpInfo> list= empInfoDao.selectByExample(example);
        if(list != null && list.size() > 0){
            returnMap.put("empData",list);
        }else {
            returnMap.put("empData", new ArrayList());
        }
        return returnMap;
    }

    /**
     * 获取所有可用餐厅
     * @return
     */
    @Override
    public List<RestaurantInfo> getRestaurantsInUse() {
        Example example = new Example(RestaurantInfo.class);
        example.createCriteria().andEqualTo("inUse","Y");
        example.orderBy("restId").desc();
        System.out.println(1);
        return restaurantInfoDao.selectByExample(example);
    }

    /**
     * 通过餐厅id 拉取可用菜单
     * @param restId
     * @return
     */
    @Override
    public List<RestMenuInfo> getMenusInUseByRestId(String restId) {
        Example example = new Example(RestMenuInfo.class);
        example.createCriteria().andEqualTo("inUse","Y").andEqualTo("restId",restId);
        example.orderBy("restId").desc();
        return restMenuInfoDao.selectByExample(example);
    }

}
