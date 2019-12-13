package com.graduation.oa.controller;

import com.graduation.oa.config.annotation.LogPub;
import com.graduation.oa.data.DeptInfo;
import com.graduation.oa.service.DeptInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptInfoController extends BaseController {
    @Autowired
    private DeptInfoService deptInfoService;

    @GetMapping("/api/departments")
    public List<DeptInfo> fetchTree(DeptInfo deptInfo) {
        return deptInfoService.fetchTree(deptInfo);
    }

    /**
     * 查询全部部门信息
     * @return
     */
    @GetMapping("/api/departments/all")
    @LogPub("展示全部部门")
    public List<DeptInfo> fetchDeptAll() {
        return deptInfoService.fetchAll();
    }

    @PostMapping("/api/departments")
    @LogPub("新增部门")
    public int create(@RequestBody DeptInfo deptInfo) {
        return deptInfoService.create(deptInfo,getLoginUser().getCode());
    }

    @PutMapping("/api/departments")
    @LogPub("修改部门")
    public int modify(@RequestBody DeptInfo deptInfo) {
        return deptInfoService.modify(deptInfo);
    }

    @DeleteMapping("/api/departments/{ids}")
    @LogPub("删除部门")
    public int remove(@PathVariable String ids) {
        return deptInfoService.remove(ids);
    }

}
