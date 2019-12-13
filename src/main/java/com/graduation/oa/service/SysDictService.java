package com.graduation.oa.service;

import com.graduation.oa.data.SysDict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SysDictService {
    List<SysDict> fetch(String code);
    List<SysDict> fetchAll();
    int create(SysDict sysDict);
    int modify(SysDict sysDict);
    int remove(String codes);
}
