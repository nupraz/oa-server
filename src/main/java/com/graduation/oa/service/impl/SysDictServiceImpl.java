package com.graduation.oa.service.impl;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.StringUtils;
import com.graduation.oa.dao.SysDictDao;
import com.graduation.oa.data.SysDict;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.service.SysDictService;
import com.graduation.oa.common.support.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;


@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class SysDictServiceImpl extends BaseService implements SysDictService {
    @Autowired
    private SysDictDao sysDictDao;

    @Override
    public List<SysDict> fetch(String code) {
        return CacheUtils.fetchDict(code);
    }

    @Override
    public List<SysDict> fetchAll() {
        Example example = new Example(SysDict.class);
        example.orderBy("code");
        return sysDictDao.selectByExample(example);
    }

    @Override
    public int create(SysDict sysDict) {
        Example example = new Example(SysDict.class);
        example.createCriteria().andEqualTo("code", sysDict.getCode());
        int cnt = sysDictDao.selectCountByExample(example);
        if (cnt > 0) {
            throw new ServiceException("字典重复");
        }
        if (StringUtils.isEmpty(sysDict.getId())) {
            sysDict.setId(StringUtils.guid());
        }
        int ret = sysDictDao.insert(sysDict);
        CacheUtils.createDict(sysDict);
        return ret;
    }

    @Override
    public int modify(SysDict sysDict) {
        int ret = sysDictDao.updateByPrimaryKey(sysDict);
        CacheUtils.modifyDict(sysDict);
        return ret;
    }

    @Override
    public int remove(String codes) {
        Example example = new Example(SysDict.class);
        example.createCriteria().andIn("code", Arrays.asList(codes.split(",")));
        int ret = sysDictDao.deleteByExample(example);
        CacheUtils.removeDict(Arrays.asList(codes.split(",")));
        return ret;
    }
}
