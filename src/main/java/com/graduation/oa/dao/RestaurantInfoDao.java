package com.graduation.oa.dao;

import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface RestaurantInfoDao extends Mapper<RestaurantInfo> {

}
