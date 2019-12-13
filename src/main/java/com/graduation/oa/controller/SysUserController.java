package com.graduation.oa.controller;

import com.graduation.oa.data.SysRole;
import com.graduation.oa.data.SysUser;
import com.graduation.oa.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
public class SysUserController extends BaseController {
    @Autowired
    private SysUserService sysUserService;

//    @PutMapping("/api/authority/roles")
//    public SysUser modify(@RequestBody SysUser sysUser) {
//        return sysUserService.modify(sysUser);
//    }

    @GetMapping("/api/users")
    public List<SysUser> fetch(SysUser sysUser) {
        return sysUserService.fetch(sysUser);
    }
    @PutMapping("/api/users/avatar")
    public int modifyAvatar(@RequestBody String avatar) {
        return sysUserService.modifyAvatar(getLoginUserId(), avatar);
    }
    @PutMapping("/api/users/settings")
    public int modifySettings(@RequestBody Map<String, Object> settings) {
        return sysUserService.modifySettings(getLoginUserId(), settings);
    }

    @PostMapping("/api/users")
    public int create(@RequestBody SysUser sysUser) throws NoSuchAlgorithmException {
        return sysUserService.create(sysUser);
    }

    @PutMapping("/api/users")
    public int modify(@RequestBody SysUser sysUser) {
        return sysUserService.modify(sysUser);
    }

    @DeleteMapping("/api/users/{ids}")
    public void remove(@PathVariable String ids) {
        sysUserService.remove(ids);
    }

    /**
     * 用户管理重置密码为666666
     * @return
     */
    @PutMapping("/api/users/{id}/password/reset")
    public int resetPass(@PathVariable("id") String id) {
        return sysUserService.resetPass(id);
    }

    /**
     * 查询用户授权角色
     * @param id
     * @return
     */
    @GetMapping("/api/users/{id}/roles")
    public Map<String, List<SysRole>> fetchGrants(@PathVariable String id) {
        return sysUserService.fetchGrants(id);
    }

    /**
     * 保存用户授权角色
     * @return
     */
    @PutMapping("/api/users/{id}/roles")
    public int saveGrants(@PathVariable String id, @RequestBody String[] roles) {
        return sysUserService.saveGrants(id, roles);
    }
}
