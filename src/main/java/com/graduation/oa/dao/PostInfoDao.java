package com.graduation.oa.dao;

import com.graduation.oa.data.PostInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Classname PostInfoDao
 * @Description
 * @Date 2019/11/6 13:53
 * @Created by yl
 */
@Repository
public interface PostInfoDao extends Mapper<PostInfo> {
    int getEmpPostCount(@Param("code") String code);
}
