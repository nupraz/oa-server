package com.graduation.oa.service.impl;

import com.graduation.oa.dao.CommonDao;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class CommonServiceImpl extends BaseService implements CommonService {
    @Autowired
    private CommonDao commonDao;
    @Override
    public Map filterMore(Map<String, Object> map) {
        //对实体类名转为下划线格式
        String entity = HumpToUnderline((String)map.get("entity"));
        map.put("entity", entity);
        //对字段列转为下划线格式
        List<String> columns = (List<String>)map.get("columns");
        List<String> columns1 = new ArrayList<>();
        for (String column:columns) {
            columns1.add(HumpToUnderline(column));
        }
        map.put("columns", columns1);
        //limit begin
        Integer begin = ((Integer) map.get("page")-1)*(Integer)map.get("limit");
        map.put("begin", begin);
        //对排序字段列转为下划线格式
        List<Map> sorts = (List<Map>)map.get("sorts");
        List<Map> sorts1 = new ArrayList<>();
        for (Map sort:sorts) {
            sort.put("name", HumpToUnderline((String)sort.get("name")));
            sorts1.add(sort);
        }
        map.put("sorts", sorts1);
        //模糊查询结果集
        List<Map> resultList = commonDao.filterMore(map);
        //数据total
        Integer total = commonDao.filterCount(map);
        List<Map> returnList = new ArrayList<>();
        //返回map集合中字段名由下划线转驼峰
        Iterator it = resultList.iterator();
        while (it.hasNext()) {
            Map map1 = (Map)it.next();
            Map map2 = new HashMap();
            for(Object key : map1.keySet()){
                map2.put(UnderlineToHump((String)key),map1.get(key));
            }
            returnList.add(map2);
        }
        Map resultMap = new HashMap();
        resultMap.put("items", returnList);
        resultMap.put("total", total);
        return resultMap;
    }

    @Override
    public void checkSort(String sort, Example example) {
        if (!StringUtils.isEmpty(sort)) {
            String[] sorts = sort.split(",");
            String sortProp = null;
            boolean sortDesc = false;
            if (sorts.length > 0) {
                logger.info("排序参数---"+sorts);
                sortProp = sorts[0];
                if (sorts.length > 1) {
                    sortDesc = sorts[1].equals("descending");
                }
                if (!sortDesc) {
                    example.orderBy(sortProp);
                } else {
                    example.orderBy(sortProp).desc();
                }
            }
        }
    }

    @Override
    public String getNewId(String idType, String id) {
            String newId = "00000";
            if(id != null && !id.isEmpty()){
                int newRecord = Integer.parseInt(id) + 1;
                newId = String.format(idType + "%05d", newRecord);
            }
            return newId;
    }

    @Override
    public String getNewNumber(String idType,String id) {
            String newNumber = "000";
            Calendar calendar = Calendar.getInstance();
            String oldYearStr = calendar.get(Calendar.YEAR)+"";
            int length = oldYearStr.length();
            String yearStr = oldYearStr.substring(2,length);
            if(id != null && !id.isEmpty()){
                int newRecord = Integer.parseInt(id) + 1;
                newNumber = String.format(idType +yearStr+ "%03d", newRecord);
            }
            return newNumber;
    }

    @Override
    public String dateSub(Date date) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format(date).substring(0, 10);
    }

    /**
     * 驼峰命名转下划线命名
     * @param para
     * @return
     */
    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }
    /**
     * 下划线命名转驼峰命名
     * @param para
     * @return
     */
    public static String UnderlineToHump(String para){
        StringBuilder result=new StringBuilder();
        String a[]=para.split("_");
        for(String s:a){
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if(result.length()==0){
                result.append(s.toLowerCase());
            }else{
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
    /**
     * 第一个字符转为大写
     */
    public static String UpperCharFirst(String para){
        return para.substring(0, 1).toUpperCase()+para.substring(1, para.length()-1);
    }
}
