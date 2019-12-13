package com.graduation.oa.controller;


import com.graduation.oa.data.SysDict;
import com.graduation.oa.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SysDictController extends BaseController {
    @Autowired
    private SysDictService sysDictService;

    //查询某项字典信息
    @GetMapping("/api/dicts/{code}")
    public List<SysDict> fetch(@PathVariable String code) {
        return sysDictService.fetch(code);
    }

    //查询字典信息
    @GetMapping("/api/dicts")
    public List<SysDict> fetchAll() {
        return sysDictService.fetchAll();
    }

    @PostMapping("/api/dicts")
    public int create(@RequestBody SysDict sysDict) {
        return sysDictService.create(sysDict);
    }

    @PutMapping("/api/dicts")
    public int modify(@RequestBody SysDict sysDict) {
        return sysDictService.modify(sysDict);
    }

    @DeleteMapping("/api/dicts/{codes}")
    public int remove(@PathVariable String codes) {
        return sysDictService.remove(codes);
    }
}
