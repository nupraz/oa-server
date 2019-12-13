package com.graduation.oa.controller;

import com.graduation.oa.data.SysRole;
import com.graduation.oa.data.ViewRole;
import com.graduation.oa.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by lihua on 2016/9/30.
 */
@RestController
public class SysRoleController extends BaseController {
    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/api/roles")
    public int create(@RequestBody SysRole sysRole) {
        return sysRoleService.create(sysRole,this.getLoginUser().getCode());
    }

    @PutMapping("/api/roles")
    public int modify(@RequestBody SysRole sysRole) {
        return sysRoleService.modify(sysRole);
    }

    @DeleteMapping("/api/roles/{ids}")
    public int remove(@PathVariable String ids) {
        return sysRoleService.remove(ids);
    }

    /**
     * 判断前端删除按钮是否显示，如果存在用户授予该角色，不显示删除按钮，返回-1
     * @param ids 逗号连接的角色id字符串
     * @return
     */
    /*@GetMapping("/api/roles/{ids}/show")
    public int removeShow(@PathVariable String ids) {
        return sysRoleService.removeShow(ids);
    }*/

    /**
     * 查询角色信息列表
     * @param viewRole
     * @return
     */
    @GetMapping("/api/roles")
    public List<ViewRole> fetch(ViewRole viewRole) {
        return sysRoleService.fetch(viewRole);
    }

    @GetMapping("/api/roles/all")
    public List<SysRole> fetchAll() {
        return sysRoleService.fetchAll();
    }

    @GetMapping("/api/roles/{id}/routes")
    public String fetchRoleRoutes(@PathVariable String id) {
        return sysRoleService.fetchRoutes(id);
    }

    @PostMapping("/api/roles/{id}/routes")
    public int saveRoleRoutes(@PathVariable String id, @RequestBody Map<String, List<String>> routes) {
        return sysRoleService.saveRoutes(id, routes);
    }
}
