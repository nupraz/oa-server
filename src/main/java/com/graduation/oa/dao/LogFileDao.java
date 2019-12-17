package com.graduation.oa.dao;

import com.graduation.oa.data.LogFile;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface LogFileDao extends Mapper<LogFile> {
    LogFile selectInfoByMd5(LogFile logFile);
    LogFile selectInfo(String fileId);
    int updateInfo(LogFile logFile);
}
