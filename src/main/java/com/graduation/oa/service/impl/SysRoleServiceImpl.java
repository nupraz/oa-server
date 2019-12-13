package com.graduation.oa.service.impl;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.redis.Cache;
import com.bestvike.commons.utils.ConvertUtils;
import com.graduation.oa.dao.SysRoleDao;
import com.graduation.oa.dao.SysUserDao;
import com.graduation.oa.dao.ViewRoleDao;
import com.graduation.oa.data.SysRole;
import com.graduation.oa.data.ViewRole;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class SysRoleServiceImpl extends BaseService implements SysRoleService {
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private ViewRoleDao viewRoleDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    @Qualifier("authorityCache")
    private Cache cache;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int create(SysRole sysRole,String user) {
        if (StringUtils.isEmpty(sysRole.getId())) {
            sysRole.setId(UUID.randomUUID().toString());
        } else {
            // 判断数据库是否是已经有这个代码
            Example example = new Example(SysRole.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("id",sysRole.getId());
            List<SysRole> roles = sysRoleDao.selectByExample(example);
            if (roles != null && roles.size() > 0){
                throw new ServiceException("角色代码重复!");
            }
        }
        if (StringUtils.isEmpty(sysRole.getStatus())) {
            sysRole.setStatus("0000");
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            sysRole.setManageTime(dateFormat.parse(dateFormat.format(date)));
        } catch (Exception e){
            e.printStackTrace();
        }
        sysRole.setManageUser(user);
        int ret = sysRoleDao.insert(sysRole);
        if (ret > 0) {
            // 刷新redis
            cache.delete("role_permissions:" + sysRole.getId());
            if (!StringUtils.isEmpty(sysRole.getPermissions())) {
                cache.putAll("role_permissions:" + sysRole.getId(), sysRole.getRoutes());
            }
        }
        return ret;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int modify(SysRole sysRole) {
        int ret = sysRoleDao.updateByPrimaryKeySelective(sysRole);
        if (ret > 0) {
            // 刷新redis
            cache.delete("role_permissions:" + sysRole.getId());
            if (!StringUtils.isEmpty(sysRole.getPermissions())) {
                cache.putAll("role_permissions:" + sysRole.getId(), sysRole.getRoutes());
            }
        }
        return ret;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int remove(String ids) {
        SysRole sysRole = new SysRole();
        sysRole.setStatus("9999");
        Example example = new Example(SysRole.class);
        example.createCriteria().andIn("id", Arrays.asList(ids.split(",")));
        int ret = sysRoleDao.updateByExampleSelective(sysRole, example);
        if (ret > 0) {
            for (String id : ids.split(",")) {
                cache.delete("role_permissions:" + id);
            }
        }
        return ret;
    }

    /*@Override
    public int removeShow(String ids) {
        //判断是否用用户授予了该角色
        List<String> roles = Arrays.asList(ids.split(","));
        int count = sysUserDao.selectIsGrantCount(roles);
        if (count != 0) {
            return -1;
        }else {
            return 1;
        }
    }*/

    @Override
    public List<ViewRole> fetch(ViewRole viewRole) {
        List <ViewRole> list = viewRoleDao.fetch(viewRole);
        return list;
        /*PageInfo<SysRole> pageInfo = MybatisUtils.page(sysRole, sysRoleDao, new ExampleCriteria() {
            @Override
            public void initCriteria(Example.Criteria criteria) {
                criteria.andEqualTo("status", "0000");
                if (!StringUtils.isEmpty(sysRole.getFuzzy())) {
                    criteria.andLike("id", "%" + sysRole.getFuzzy() + "%");
                    criteria.orLike("name", "%" + sysRole.getFuzzy() + "%");
                } else {
                    if (!StringUtils.isEmpty(sysRole.getId())) {
                        criteria.andEqualTo("id", sysRole.getId());
                    }
                    if (!StringUtils.isEmpty(sysRole.getName())) {
                        criteria.andLike("name", "%" + sysRole.getName() + "%");
                    }
                }
            }
        });
        return pageInfo.getList();*/
    }

    @Override
    public List<SysRole> fetchAll() {
        return sysRoleDao.fetchAll();
    }

    @Override
    public String fetchRoutes(String roleId) {
        SysRole sysRole = sysRoleDao.selectByPrimaryKey(roleId);
        if (sysRole != null) {
            return sysRole.getPermissions();
        }
        return null;
    }

    @Override
    @Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public int saveRoutes(String roleId, Map<String, List<String>> routes) {
        String permissions = ConvertUtils.getString(routes);
        SysRole sysRole = new SysRole();
        sysRole.setId(roleId);
        sysRole.setPermissions(permissions);
        int ret = sysRoleDao.updateByPrimaryKeySelective(sysRole);
        if (ret > 0) {
            // 刷新redis
            cache.delete("role_permissions:" + roleId);
            if (!StringUtils.isEmpty(permissions)) {
                cache.putAll("role_permissions:" + roleId, ConvertUtils.getBean(permissions, Map.class));
            }
        }
        return ret;
    }

    @Override
    public List<SysRole> fetchAllPermissions() {
        return sysRoleDao.fetchAllPermissions();
    }
}
