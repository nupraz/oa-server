package com.graduation.oa.dao;

//import com.graduation.oa.data.projEntity.ProjInfo;
import com.graduation.oa.data.projEntity.ProjWeeklyNew;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ProjWeeklyNewDao extends Mapper<ProjWeeklyNew> {
    /**
     * 分页查询
     * @param fuzzy
     * @param projId
     * @param projName
     * @param projManage
     * @param empName
     * @param sortTemp
     * @param column
     * @return
     */
    List<ProjWeeklyNew> getProjWeeklyNewInfo(@Param("fuzzy") String fuzzy,
                                             @Param("projId") String projId,
                                             @Param("projName") String projName,
                                             @Param("projManage") String projManage,
                                             @Param("empName") String empName,
                                             @Param("dateWeek") String dateWeekTemp,
                                             @Param("sort") String sortTemp,
                                             @Param("column") String column);
    /*//查询项目名称id项目类型
    List<ProjInfo> fetchProjInfo(@Param("projName")String projName, @Param("type")String type);*/
}