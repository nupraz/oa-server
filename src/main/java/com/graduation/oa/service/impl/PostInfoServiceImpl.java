package com.graduation.oa.service.impl;

import com.bestvike.commons.exception.ServiceException;
import com.graduation.oa.dao.PostInfoDao;
import com.graduation.oa.data.PostInfo;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.service.PostInfoService;
import com.graduation.oa.support.ExampleCriteria;
import com.graduation.oa.support.MybatisUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classname PostInfoServiceImpl
 * @Description 增删改查
 * @Date 2019/11/6 13:52
 * @Created by yl
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class PostInfoServiceImpl extends BaseService implements PostInfoService {
    @Autowired
    private PostInfoDao postInfoDao;
    @Override
    public List<PostInfo> fetchAll() {
        Example example = new Example(PostInfo.class);
        example.createCriteria().andEqualTo("status", "0000");
        return postInfoDao.selectByExample(example);
    }

    @Override
    public List<PostInfo> fetch(PostInfo postInfo) {
        PageInfo<PostInfo> pageInfo =  MybatisUtils.page(postInfo, postInfoDao, new ExampleCriteria() {
            @Override
            public void initCriteria(Example.Criteria criteria) {
                if (!StringUtils.isEmpty(postInfo.getFuzzy())) {
                    criteria.andLike("code", "%" + postInfo.getFuzzy() + "%");
                    criteria.orLike("name", "%" + postInfo.getFuzzy() + "%");
                } else {
                    if (!StringUtils.isEmpty(postInfo.getCode())) {
                        criteria.andEqualTo("code", postInfo.getCode());
                    }
                    if (!StringUtils.isEmpty(postInfo.getName())) {
                        criteria.andLike("name", "%" + postInfo.getName() + "%");
                    }
                }
            }
        });
        return pageInfo.getList();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int create(PostInfo postInfo,String user) {
        PostInfo temp = postInfoDao.selectByPrimaryKey(postInfo.getCode());
        if (temp != null) {
            throw new ServiceException("该岗位编号已存在");
        }
        postInfo.setStatus("0000");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            postInfo.setManageTime(dateFormat.parse(dateFormat.format(date)));
        } catch (Exception e){
            e.printStackTrace();
        }
        postInfo.setManageUser(user);
        return postInfoDao.insert(postInfo);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int modify(PostInfo postInfo) {
        return postInfoDao.updateByPrimaryKey(postInfo);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map remove(String codes) {
        Map resultMap = new HashMap();
        String posts[] = codes.split(",");
        StringBuffer stringBuffer = new StringBuffer();
        for(String code:posts){
            int empPostCount = postInfoDao.getEmpPostCount(code);
            if(empPostCount == 0) {
                postInfoDao.deleteByPrimaryKey(code);
            } else {
                Example example = new Example(PostInfo.class);
                example.createCriteria().andEqualTo("code",code);
                PostInfo postInfo = postInfoDao.selectOneByExample(example);
                stringBuffer.append(postInfo.getName()+"岗位下存在员工，请修改员工信息后重试");
            }
        }
        String msg = stringBuffer.toString();
        if(msg.length()>0){
            resultMap.put("retCode","9999");
            resultMap.put("level","warning");
            resultMap.put("msg",msg);
        } else {
            resultMap.put("retCode", "0000");
            resultMap.put("level", "success");
            resultMap.put("msg", "删除成功");
        }
        return resultMap;
    }
}
