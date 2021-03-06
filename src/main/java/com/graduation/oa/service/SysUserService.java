package com.graduation.oa.service;

import com.graduation.oa.data.SysRole;
import com.graduation.oa.data.SysUser;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Service
public interface SysUserService {
    List<SysUser> fetch(SysUser sysUser);
    int modifyAvatar(String id, String avatar);
    int modifySettings(String id, Map<String, Object> settings);
    int create(SysUser sysUser) throws NoSuchAlgorithmException;
    int modify(SysUser sysUser);
    void remove(String ids);
    int resetPass(String id);
    Map<String, List<SysRole>> fetchGrants(String id);
    int saveGrants(String id, String[] roles);
}
