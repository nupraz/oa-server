package com.graduation.oa.common.support;

import com.graduation.oa.common.redis.Cache;
import com.graduation.oa.data.DeptInfo;
import com.graduation.oa.data.SysDict;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.List;

public class CacheUtils {
    private static ApplicationContext applicationContext;
    private static Cache cache;

    /////////////////////////////////////// 字典 ////////////////////////////////////////////////////////////
    public static void createDict(SysDict sysDict) {
        cache.set("dicts:list:" + sysDict.getCode(), sysDict.getChildren());
        List<SysDict> children = sysDict.getChildren();
        if (children != null && children.size() > 0) {
            for (SysDict child : children) {
                cache.put("dicts:hash:" + sysDict.getCode(), child.getCode(), child.getName());
            }
        }
    }
    public static void modifyDict(SysDict sysDict) {
        removeDict(sysDict.getCode());
        createDict(sysDict);
        /*cache.delete("dicts:list:" + sysDict.getCode());
        cache.set("dicts:list:" + sysDict.getCode(), sysDict.getChildren());

        cache.delete("dicts:hash:" + sysDict.getCode());
        List<SysDict> children = sysDict.getChildren();
        if (children != null && children.size() > 0) {
            for (SysDict child : children) {
                cache.put("dicts:hash:" + sysDict.getCode(), child.getCode(), child.getName());
            }
        }*/
    }
    public static void removeDict(List<String> list) {
        for (String code : list) {
            removeDict(code);
        }
    }
    public static void removeDict(String code) {
        cache.delete("dicts:list:" + code);
        cache.delete("dicts:hash:" + code);
    }

    public static List<SysDict> fetchDict(String type) {
        return cache.get("dicts:list:" + type, List.class);
    }

    public static String transDict(String type, String code) {
        if (cache == null || StringUtils.isEmpty(code)) {
            return code;
        }
        return cache.getMapValue("dicts:hash:" + type, code, String.class);
    }

    /////////////////////////////////////// 部门 ////////////////////////////////////////////////////////////
    public static void createDept(DeptInfo deptInfo) {
        cache.put("depts", deptInfo.getId(), deptInfo.getName());
    }
    public static void modifyDept(DeptInfo dept) {
        removeDept(dept.getId());
        createDept(dept);
    }
    public static void removeDept(List<String> list) {
        for (String id : list) {
            removeDept(id);
        }
    }
    public static void removeDept(String id) {
        cache.deleteMapKey("depts", id);
    }

    public static String transDept(String id) {
        if (cache == null || StringUtils.isEmpty(id)) {
            return id;
        }
        return cache.getMapValue("depts", id, String.class);
    }

    public static void init(ApplicationContext applicationContext) {
        CacheUtils.applicationContext = applicationContext;
        cache = getBean("cache", Cache.class);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }
}
