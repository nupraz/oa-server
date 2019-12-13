package com.graduation.oa.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommonDao {
    List<Map> filterMore(Map<String, Object> map);
    Integer filterCount(Map<String, Object> map);
}
