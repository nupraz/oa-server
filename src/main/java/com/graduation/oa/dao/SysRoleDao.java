package com.graduation.oa.dao;

import com.graduation.oa.data.SysRole;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SysRoleDao extends Mapper<SysRole> {
    List<SysRole> fetchAll();
    List<SysRole> fetchAllPermissions();
}
