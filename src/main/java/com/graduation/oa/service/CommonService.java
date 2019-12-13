package com.graduation.oa.service;

import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Map;

public interface CommonService {
    Map filterMore(Map<String, Object> map);

    /**
     * 统一封装pageHelper处理排序参数逻辑.
     * @param sort
     * @param example
     */
    void checkSort(String sort, Example example);
    /**
     * 自动获取编号
     * @param idType
     * @param id
     * @return String
     */
    String getNewId(String idType, String id);
    /**
     * 封装指定id
     * @param idType
     * @param id
     * @return String
     */
    String getNewNumber(String idType, String id);
    /**
     * 封装时间截取
     */
    String dateSub(Date date);
}
