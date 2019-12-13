package com.graduation.oa.controller;

import com.graduation.oa.config.annotation.LogPub;
import com.graduation.oa.data.PostInfo;
import com.graduation.oa.service.PostInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Classname PostInfoController
 * @Description
 * @Date 2019/11/6 14:07
 * @Created by yl
 */
@RestController
public class PostInfoController extends BaseController {
    @Autowired
    private PostInfoService postInfoService;

    @GetMapping("/api/postInfos/all")
    @LogPub("查询全部岗位信息")
    public List<PostInfo> fetchAll() {
        return postInfoService.fetchAll();
    }

    @GetMapping("/api/postInfos")
    @LogPub("查询分页岗位信息")
    public List<PostInfo> fetch(PostInfo postInfo) {
        return postInfoService.fetch(postInfo);
    }

    @PostMapping("/api/postInfos")
    @LogPub("新增岗位信息")
    public int create(@RequestBody PostInfo postInfo) {
        return postInfoService.create(postInfo,this.getLoginUser().getCode());
    }

    @PutMapping("/api/postInfos")
    @LogPub("修改岗位信息")
    public int modify(@RequestBody PostInfo postInfo) {
        return postInfoService.modify(postInfo);
    }

    @DeleteMapping("/api/postInfos/{codes}")
    @LogPub("删除一个或多个岗位信息")
    public Map remove(@PathVariable String codes) {
        return postInfoService.remove(codes);
    }
}
