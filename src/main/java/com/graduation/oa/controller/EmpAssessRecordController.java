package com.graduation.oa.controller;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.FileUtils;
import com.graduation.oa.config.annotation.LogPub;
import com.graduation.oa.data.EmpAssessRecord;
import com.graduation.oa.service.EmpAssessRecordService;
import com.graduation.oa.support.ExcelUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmpAssessRecordController extends BaseController {


    @Value("${app.file.exportPath:}")
    private String exportPath;
    @Value("${app.file.template-path:}")
    private String templatePath;
    @Autowired
    private EmpAssessRecordService empAssessRecordService;
    //员工评价报表

    @GetMapping("/api/emprecord")
    @LogPub("员工评价记录查询")
    public List<EmpAssessRecord> fetchEmpReport(EmpAssessRecord empAssessRecord) {
        empAssessRecord.setEmpId(this.getLoginUserId());
        return empAssessRecordService.fetchCord(empAssessRecord);
    }

    //我的下属评价月报表导出
    @PostMapping("/api/emprecord/exports")
    public Map<String, String> exportEmprecordExcel(@RequestBody Map<String, List<EmpAssessRecord>> lists, HttpServletResponse response) throws IOException {
        //获取数据
        List<EmpAssessRecord> list = lists.get("list");
        //excel标题
        String[] title = {"序号", "部门", "岗位", "姓名", "评分", "备注"};
        List<String[]> datas = new ArrayList<>();
        if (list != null && list.size() > 0) {
            int i = 1;
            for (EmpAssessRecord empAssessRecord : list) {
                String[] data = new String[6];
                data[0] = String.valueOf(i);
                data[1] = ExcelUtil.NullToEmpty(empAssessRecord.getDeptName());
                data[2] = ExcelUtil.NullToEmpty(empAssessRecord.getGradeName());
                data[3] = ExcelUtil.NullToEmpty(empAssessRecord.getEmpName());
                data[4] = ExcelUtil.NullToEmpty(empAssessRecord.getEmpScore() + "");
                data[5] = ExcelUtil.NullToEmpty(empAssessRecord.getAssessRemark());
                datas.add(data);
                i++;
            }
        }
        String path = exportPath + "/" + getLoginUserId();
        String fileName = list.get(0).getAssessMonth().substring(0, 4) + "年" + list.get(0).getAssessMonth().substring(4, 6) + "月下属员工评价记录月报表";
        ExcelUtil.exportTravelEmpDataToExcel(title, datas, path, fileName);
        //下载输出
        Map<String, String> map = new HashMap<>();
        map.put("fileName", path + "/" + fileName + ".xls");
        map.put("pathName", path);
        return map;
    }

    //下载员工绩效导入模板
    @PostMapping("/api/emp/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody Map<String, String> param) throws IOException {
        String fileName = param.get("fileName");
        FileUtils.download(request, response,
                FileUtils.getInputStream(templatePath + File.separator + fileName), "empRecord.xls");
    }

    //员工绩效回显专用导入
    @PostMapping("/api/emp/imports")
    public Map<String, Object> importDept(@RequestParam(value = "file") MultipartFile[] file,
                                          @RequestParam(value = "fileSource") String[] fileSource,
                                          @RequestParam(value = "manageUser") String user,
                                          @RequestParam(value = "path") String path) throws InvalidFormatException, SAXException, IOException {
        Map<String, Object> result = new HashMap<>();
        String fileName = file[0].getOriginalFilename();
        user = this.getLoginUserId();
        try {
            result = empAssessRecordService.importSale(file[0], fileSource[0], path, user);
        } catch (ServiceException e) {
            logger.info("员工绩效文件上传发生异常", e);
            result.put("retCode", "9999");
            result.put("level", "error");
            result.put("retMsg", "导入模板格式存在问题，请使用正确的模板导入");
        }
        return result;
    }

    //员工绩效真实导入
    @PostMapping("/api/emp/insert")
    @LogPub("员工绩效真实导入")
    public Map<String, Object> importEmpData(@RequestBody Map<String, List<Object>> map) {
        return empAssessRecordService.insertObj(this.getLoginUserId(), map);

    }

    //查询所有员工考核
    @GetMapping("/api/empAssessRecord")
    public List<EmpAssessRecord> fetch(EmpAssessRecord empAssessRecord) {
        return empAssessRecordService.fetch(empAssessRecord, this.getLoginUser().getCode());
    }

    //暂存、保存
    @LogPub("暂存、保存")
    @PostMapping("/api/empAssessRecord/save")
    public Map<String, Object> saveEmpAssessRecord(@RequestBody String data) {
        return empAssessRecordService.dealEmpAssess(this.getLoginUserId(), data);
    }

    //查询登录用户所在部门上月最终得分
    @GetMapping("/api/dept/score")
    @LogPub("查询登录用户所在部门上月最终得分")
    public Map<String, Object> fetchDeptScore() {
        return empAssessRecordService.fetchDeptScore(this.getLoginUserId());
    }

    //上报员工评价记录
    @PostMapping("/api/emprecord/save")
    @LogPub("上报员工评价记录")
    public Map<String, Object> saveEmprecord(@RequestBody Map<String, List<EmpAssessRecord>> map) {
        List<EmpAssessRecord> list = map.get("list");
        return empAssessRecordService.saveEmprecord(list);
    }

    //上报员工评价记录导出
    @PostMapping("/api/allemprecord/exports")
    public Map<String, String> exportAllEmprecordExcel(@RequestBody Map<String, List<EmpAssessRecord>> maps, HttpServletResponse response) throws IOException {
        Boolean checkIsRecord = empAssessRecordService.checkIsRecord();
        if(!checkIsRecord){
            throw new ServiceException("尚存在未完成上报的员工评价,不能导出数据,请仔细检查");
        }
        //excel标题
        List<EmpAssessRecord> list = maps.get("list");

        //部门、岗位、 姓名 ,奖惩分,最终得分 、备注（评分原因
        String[] title = {"编号", "部门", "岗位", "姓名", "奖惩分", "最终得分", "备注"};
        List<String[]> datas = new ArrayList<>();
        if (list != null && list.size() > 0) {
            int i = 1;
            for (EmpAssessRecord empAssessRecord : list) {
                String[] data = new String[7];
                data[0] = ExcelUtil.NullToEmpty(empAssessRecord.getEmpId());
                data[1] = ExcelUtil.NullToEmpty(empAssessRecord.getDeptName());
                data[2] = ExcelUtil.NullToEmpty(empAssessRecord.getPostName());
                data[3] = ExcelUtil.NullToEmpty(empAssessRecord.getEmpName());
                data[4] = empAssessRecord.getRewardScore() == null ? "" : empAssessRecord.getRewardScore() + "";
                data[5] = empAssessRecord.getScore() == null ? "" : empAssessRecord.getScore() + "";
                data[6] = ExcelUtil.NullToEmpty(empAssessRecord.getAssessRemark());
                datas.add(data);
                i++;
            }
        }
        String path = exportPath + "/" + getLoginUserId();
        String fileName = list.get(0).getAssessMonth().substring(0, 4) + "年" + list.get(0).getAssessMonth().substring(4, 6) + "月上报员工评价报表";
        ExcelUtil.exportTravelAllEmpDataToExcel(title, datas, path, fileName);
        //下载输出
        Map<String, String> map = new HashMap<>();
        map.put("fileName", path + "/" + fileName + ".xls");
        map.put("pathName", path);
        return map;
    }

    /**
     * 员工考核上报之修改
     *
     * @Param: [data]
     * @return: java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Date: 2019/11/22
     */
    @LogPub("员工考核上报之修改")
    @PutMapping("/api/empAssessRecord/updateEmpAssRecord")
    public void updateEmpAssRecord(@RequestBody String data) {
        empAssessRecordService.updateEmpAssRecord(this.getLoginUserId(), data);
    }

    /**
     * 员工考核上报之测算
     *
     * @Param: [data]
     * @return: java.util.Map<java.lang.String   ,   java.lang.Object>
     * @Date: 2019/11/22
     */
    @LogPub("员工考核上报之测算")
    @PostMapping("/api/empAssessRecord/reportingMeasurement")
    public void reportingMeasurement(@RequestBody String data) {
        empAssessRecordService.reportingMeasurement(this.getLoginUserId(), data);
    }
   /* @LogPub("参加评价的员工个数")
    @PostMapping("/api/empAssessRecord/empNum")
    public int getEmpNumIsOn(){
       return empAssessRecordService.getEmpNumIsOn();
    }*/
}
