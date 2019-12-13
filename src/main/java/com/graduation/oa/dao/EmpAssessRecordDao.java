package com.graduation.oa.dao;

import com.graduation.oa.data.EmpAssessRecord;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface EmpAssessRecordDao extends Mapper<EmpAssessRecord> {
    List<EmpAssessRecord> fetch(EmpAssessRecord empAssessRecord);
    List<EmpAssessRecord> fetchAll(EmpAssessRecord empAssessRecord);

    List<EmpAssessRecord> selectByCondition(EmpAssessRecord empAssessRecord);
    /*int fetchThreeLevel(EmpAssessRecord empAssessRecord);*/
}