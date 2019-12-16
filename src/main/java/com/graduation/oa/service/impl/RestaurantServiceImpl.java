package com.graduation.oa.service.impl;

import com.graduation.oa.common.util.StringUtils;
import com.graduation.oa.dao.RestMenuInfoDao;
import com.graduation.oa.dao.RestaurantInfoDao;
import com.graduation.oa.dao.StartOrderInfoDao;
import com.graduation.oa.data.empOrderEntity.RestMenuInfo;
import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import com.graduation.oa.data.empOrderEntity.StartOrderInfo;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.service.CommonService;
import com.graduation.oa.service.RestaurantService;
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
public class RestaurantServiceImpl extends BaseService implements RestaurantService {
    @Autowired
    private CommonService commonService;
    @Autowired
    private RestaurantInfoDao restaurantInfoDao;
    @Autowired
    private RestMenuInfoDao restMenuInfoDao;
    @Autowired
    private StartOrderInfoDao startOrderInfoDao;
    /**
     * 查询餐厅信息
     * @param fuzzy
     * @param restName
     * @param sort
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<RestaurantInfo> getRestInfo(String fuzzy, String restName, String sort, Integer page, Integer limit) {

        PageInfo<RestaurantInfo> restaurantInfoPageInfo = PageHelper.startPage(page, limit).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                Example example = new Example(RestaurantInfo.class);
                Example.Criteria criteria2 = example.and();
                example.orderBy("inUse").desc();

                if (!StringUtils.isEmpty(fuzzy)) {
                    criteria2.orLike("restName", "%" + fuzzy + "%");
                } else {
                    if (!StringUtils.isEmpty(restName)) {
                        criteria2.orEqualTo("restName", restName);
                    }
                }
                commonService.checkSort(sort, example);
                restaurantInfoDao.selectByExample(example);
            }
        });
        return restaurantInfoPageInfo;
    }
    /**
     * 新增餐厅信息
     * @param restaurantInfo
     * @param userName
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createRestInfo(RestaurantInfo restaurantInfo,String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        restaurantInfo.setRestId(UUID.randomUUID().toString());
        //设置日期格式
        restaurantInfo.setManageTime(new Date());
        restaurantInfo.setManageUser(userName);
        restaurantInfoDao.insert(restaurantInfo);

        resultMap.put("retCode","00");
        resultMap.put("level","success");
        resultMap.put("retMsg","新增成功！");
        return resultMap;
    }

    /**
     * 修改餐厅信息
     * @param restaurantInfo
     * @param userName
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> modifyRestInfo(RestaurantInfo restaurantInfo,String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        Date date=new Date();
        List<StartOrderInfo> list = startOrderInfoDao.selectIsHaveOrder(date);
        if(list != null && list.size() > 0){
            resultMap.put("retCode","99");
            resultMap.put("level","error");
            resultMap.put("retMsg","修改失败！现在为订餐时间，请结束订餐后进行修改");
        } else {
            //修改这条数据
            restaurantInfo.setManageUser(userName);
            restaurantInfo.setManageTime(new Date());
            restaurantInfoDao.updateByPrimaryKey(restaurantInfo);
            resultMap.put("retCode", "00");
            resultMap.put("level", "success");
            resultMap.put("retMsg", "修改成功！");
        }
        return resultMap;
    }
    /**
     * 删除餐厅信息
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String,Object> deleteRestInfo(String ids,String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        //删除菜单
        Example example1=new Example(RestMenuInfo.class);
        example1.createCriteria().andEqualTo("restId",ids);
        restMenuInfoDao.deleteByExample(example1);
        //删除餐厅
        Example example=new Example(RestaurantInfo.class);
        example.createCriteria().andEqualTo("restId",ids);
        restaurantInfoDao.deleteByExample(example);
        resultMap.put("retCode","00");
        resultMap.put("level","success");
        resultMap.put("retMsg","删除成功！");
        return resultMap;
    }
    /**
     * 查询菜单信息
     * @param fuzzy
     * @param menuName
     * @param sort
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageInfo<RestMenuInfo> fetchMenuInfo(String fuzzy, String restId, String menuName,String sort, Integer page, Integer limit) {

        PageInfo<RestMenuInfo> menuInfoPageInfo = PageHelper.startPage(page, limit).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                Example example = new Example(RestMenuInfo.class);
                Example.Criteria criteria2 = example.and();
                example.orderBy("inUse").desc();
                Example.Criteria criteria=example.and();
                criteria.andEqualTo("restId",restId);

                if (!StringUtils.isEmpty(fuzzy)) {
                    criteria2.orLike("menuName", "%" + fuzzy + "%");
                    criteria2.orLike("menuPrice", "%" + fuzzy + "%");
                } else {
                    if (!StringUtils.isEmpty(menuName)) {
                        criteria2.orEqualTo("menuName", menuName);
                    }
                }
                commonService.checkSort(sort, example);
                restMenuInfoDao.selectByExample(example);
            }
        });
        return menuInfoPageInfo;
    }
    /**
     * 新增菜单信息
     * @param restMenuInfo
     * @param userName
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createMenuInfo(RestMenuInfo restMenuInfo,String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        restMenuInfo.setMenuId(UUID.randomUUID().toString());
        //设置日期格式
        restMenuInfo.setManageTime(new Date());
        restMenuInfo.setManageUser(userName);
        restMenuInfoDao.insert(restMenuInfo);

        resultMap.put("retCode","00");
        resultMap.put("level","success");
        resultMap.put("retMsg","新增成功！");
        return resultMap;
    }

    /**
     * 修改菜单信息
     * @param restMenuInfo
     * @param userName
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> modifyMenuInfo(RestMenuInfo restMenuInfo,String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        Date date=new Date();
        List<StartOrderInfo> list = startOrderInfoDao.selectIsHaveOrder(date);
        if(list != null && list.size() > 0){
            resultMap.put("retCode","99");
            resultMap.put("level","error");
            resultMap.put("retMsg","修改失败!请结束当前订餐,然后进行修改");
        } else {
            restMenuInfo.setManageUser(userName);
            restMenuInfo.setManageTime(new Date());
            restMenuInfoDao.updateByPrimaryKey(restMenuInfo);
            resultMap.put("retCode", "00");
            resultMap.put("level", "success");
            resultMap.put("retMsg", "修改成功！");
        }
        return resultMap;
    }
    /**
     * 删除菜单信息
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String,Object> deleteMenuInfo(String ids,String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        Example example=new Example(RestMenuInfo.class);
        example.createCriteria().andEqualTo("menuId",ids);
        restMenuInfoDao.deleteByExample(example);
        resultMap.put("retCode","00");
        resultMap.put("level","success");
        resultMap.put("retMsg","删除成功！");
        return resultMap;
    }

    /**
     * 发起订餐
     * @param startOrderInfo
     * @param userName
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> createOrderInfo(StartOrderInfo startOrderInfo, String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        //数据库中是否有在途订餐
        Date date=new Date();
        List<StartOrderInfo> list = startOrderInfoDao.selectIsHaveOrder(date);
        if(list != null && list.size() > 0){
            resultMap.put("retCode","99");
            resultMap.put("level","error");
            resultMap.put("retMsg","新增失败！已有在途订餐");
        } else {
            //发起订餐
            startOrderInfo.setSysGuid(UUID.randomUUID().toString());
            startOrderInfo.setManageTime(new Date());
            startOrderInfo.setManageUser(userName);
            startOrderInfoDao.insert(startOrderInfo);

            resultMap.put("retCode","00");
            resultMap.put("level","success");
            resultMap.put("retMsg","新增成功！");
        }
        return resultMap;
    }
    /**
     * 取消订餐
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String,Object> deleteOrderInfo(String userName) {
        Map<String,Object> resultMap = new HashMap<>();
        Example example = new Example(StartOrderInfo.class);
        example.createCriteria().andLessThan("startDate",new Date()).andGreaterThan("endDate",new Date());
        example.setDistinct(true);
        startOrderInfoDao.deleteByExample(example);

        resultMap.put("retCode","00");
        resultMap.put("level","success");
        resultMap.put("retMsg","删除成功！");
        return resultMap;
    }

    /**
     * 查询是否正在订餐
     * @return
     */
    @Override
    public Map<String, Object> getIsHaveOrder() {
        Map<String,Object> resultMap = new HashMap<>();
        if(startOrderInfoDao.selectIsHaveOrder(new Date()).isEmpty()){
            resultMap.put("isShowStartOrder",true);
            resultMap.put("isShowCancelOrder",false);
        }else {
            resultMap.put("isShowStartOrder",false);
            resultMap.put("isShowCancelOrder",true);
        }
        return resultMap;
    }

    /**
     * 获取所有预设订餐信息
     * @return
     */
    @Override
    public PageInfo<StartOrderInfo> getAllPresetTime(String fuzzy, Integer page, Integer limit) {

        PageInfo<StartOrderInfo> presetTimeInfoPageInfo = PageHelper.startPage(page, limit).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                Example example = new Example(StartOrderInfo.class);
                example.createCriteria().andGreaterThan("startDate", new Date());
                Example.Criteria criteria = example.and();
                example.orderBy("startDate").desc();
                if (!StringUtils.isEmpty(fuzzy)) {
                    criteria.orLike("startDate", "%" + fuzzy + "%");
                    criteria.orLike("endDate", "%" + fuzzy + "%");
                }
                startOrderInfoDao.selectByExample(example);
            }
        });
        return presetTimeInfoPageInfo;
    }

    /**
     * 删除所选预设订餐信息
     * @param ids
     * @param name
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> deletePresetTimeInfo(String ids, String name) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<String> sysGuids = Arrays.asList(ids.split(","));
            for(String sysGuid: sysGuids){
                Example example = new Example(StartOrderInfo.class);
                example.createCriteria().andEqualTo("sysGuid", sysGuid);
                startOrderInfoDao.deleteByExample(example);
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

}
