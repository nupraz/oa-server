package com.graduation.oa.dao;

import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import com.graduation.oa.data.empOrderEntity.StartOrderInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface StartOrderInfoDao extends Mapper<StartOrderInfo> {
    List<StartOrderInfo> selectNowOrder(RestaurantInfo restaurantInfo);
    List<StartOrderInfo> selectIsHaveOrder(Date date);
    List<StartOrderInfo> selectRestAndMenuInfoIsHaveOrder(Date date);
    List<StartOrderInfo> selectAllMenuInfo();

}
