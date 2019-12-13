package com.graduation.oa.service;

import com.graduation.oa.data.SysRole;
import com.graduation.oa.data.ViewRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface SysRoleService {
    int create(SysRole sysRole,String user);
    int modify(SysRole sysRole);
    int remove(String ids);
    // int removeShow(String ids);
    List<ViewRole> fetch(ViewRole viewRole);
    List<SysRole> fetchAll();
    String fetchRoutes(String roleId);
    int saveRoutes(String roleId, Map<String, List<String>> routes);
    List<SysRole> fetchAllPermissions();
}
