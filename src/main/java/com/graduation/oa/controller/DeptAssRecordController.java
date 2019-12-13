package com.graduation.oa.controller;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.FileUtils;
import com.graduation.oa.config.annotation.LogPub;
import com.graduation.oa.data.AssessReportInfo;
import com.graduation.oa.data.DeptAssRecord;
import com.graduation.oa.data.SysUser;
import com.graduation.oa.service.DeptAssRecordService;
import com.graduation.oa.support.ExcelUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 部门评价
* @Param:
* @return:
* @Date: 2019/11/2
*/
@RestController
public class DeptAssRecordController extends BaseController {
    @Autowired
    private DeptAssRecordService deptAssRecordService;
    @Value("${app.file.exportPath:}")
    private String exportPath;
    @Value("${app.file.template-path:}")
    private String templatePath;

    //部门绩效月报表
    @GetMapping("/api/depreport")
    @LogPub("部门绩效月报表")
    public List<AssessReportInfo> fetchDeptReport(AssessReportInfo assessReportInfo) {
        assessReportInfo.setUserId(this.getLoginUserId());
        return deptAssRecordService.fetch(assessReportInfo);
    }

    //评价记录月报表
    @GetMapping("/api/deprecord")
    @LogPub("评价记录月报表")
    public List<DeptAssRecord> fetchDeptReport(DeptAssRecord deptAssRecord) {
        deptAssRecord.setJuryId(this.getLoginUserId());
        return deptAssRecordService.fetch(deptAssRecord);
    }

    //评价记录月报表导出
    @PostMapping("/api/deprecord/exports")
    public Map<String, String> exportDeprecordExcel(@RequestBody Map<String, List<DeptAssRecord>> lists, HttpServletResponse response) throws IOException {
        //System.out.println(lists.get("list").size());
        //获取数据
        List<DeptAssRecord> list = lists.get("list");
        //excel标题
        String[] title = {"部门", "评价", "协同评价扣分", "协调评价扣分原因", "最终得分"};
        List<String[]> datas = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (DeptAssRecord deptAssRecord : list) {
                String[] data = new String[5];
                data[0] = ExcelUtil.NullToEmpty(deptAssRecord.getDepName());
                data[1] = ExcelUtil.NullToEmpty(deptAssRecord.getTaskLevel());
                data[2] = ExcelUtil.NullToEmpty(deptAssRecord.getCooperateScore() + "");
                data[3] = ExcelUtil.NullToEmpty(deptAssRecord.getCooperateRemark());
                data[4] = ExcelUtil.NullToEmpty(deptAssRecord.getAssessScore() + "");
                datas.add(data);
            }
        }
        String path = exportPath + "/" + getLoginUserId();
        String fileName = "我的评价记录月报表.xls";
        ExcelUtil.exportTravelmyDataToExcel(title, datas, path, fileName);
        //下载输出
        Map<String, String> map = new HashMap<>();
        map.put("fileName", path + "/" + fileName);
        map.put("pathName", path);
        return map;
    }

    //部门绩效月报表导出
    @PostMapping("/api/depreport/exports")
    public Map<String, String> exportDepreportExcel(@RequestBody Map<String, List<AssessReportInfo>> lists, HttpServletResponse response) throws IOException {
        //获取数据
        List<AssessReportInfo> list = lists.get("list");
        //excel标题
        String[] title = {"部门", "最终得分"};
        List<String[]> datas = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (AssessReportInfo assessReportInfo : list) {
                String[] data = new String[2];
                data[0] = ExcelUtil.NullToEmpty(assessReportInfo.getDeptName());
                data[1] = ExcelUtil.NullToEmpty(assessReportInfo.getAssessScore() + "");
                datas.add(data);
            }
        }
        String path = exportPath + "/" + getLoginUserId();
        String fileName =list.get(0).getAssessMonth().substring(0,4)+"年"+list.get(0).getAssessMonth().substring(4,6)+ "月综合服务中心绩效评价结果";
        ExcelUtil.exportTravelDataToExcel(title, datas, path, fileName);
        //下载输出
        Map<String, String> map = new HashMap<>();
        map.put("fileName", path + "/" + fileName+".xls");
        map.put("pathName", path);
        return map;
    }

    //下载预导出文件
    @GetMapping("/api/report/download")
    public HttpServletResponse downloadResourceCode(HttpServletResponse response, @RequestParam("path") String path, @RequestParam("pathName") String pathName) {
        try {
            //参数编码解码解决前台location.href 400错误
            path = URLDecoder.decode(path, "UTF-8");
            pathName = URLDecoder.decode(pathName, "UTF-8");
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            filename = URLEncoder.encode(filename, "UTF-8");

            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header  测试服才需要解决跨域问题
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers",
                    "X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,token");
            response.setContentType("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                ExcelUtil.deleteFileDirectory(pathName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //部门绩效模板下载
    @PostMapping("/api/dep/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody Map<String, String> param) throws IOException {
        String fileName = param.get("fileName");
        FileUtils.download(request, response,
                FileUtils.getInputStream(templatePath + File.separator + fileName), "deptRecord.xls");
    }

    //部门绩效回显专用导入
    @PostMapping("/api/dep/imports")
    public Map<String, Object> importDept(@RequestParam(value = "file") MultipartFile[] file,
                                          @RequestParam(value = "fileSource") String[] fileSource,
                                          @RequestParam(value = "manageUser") String user,
                                          @RequestParam(value = "path") String path) throws InvalidFormatException, SAXException, IOException {
        Map<String, Object> result = new HashMap<>();
        String fileName = file[0].getOriginalFilename();
        user = this.getLoginUserId();
        try {
            result = deptAssRecordService.importSale(file[0], fileSource[0], path, user);
        } catch (ServiceException e) {
            logger.info("部门绩效文件上传发生异常", e);
            result.put("retCode", "9999");
            result.put("level", "error");
            result.put("retMsg", "导入模板格式存在问题，请使用正确的模板导入");
        }
        return result;
    }

    //部门绩效真实导入
    @PostMapping("/api/dept/insert")
    @LogPub("部门绩效真实导入")
    public Map<String, Object> importDeptData(@RequestBody Map<String, List<Object>> map) {
        return deptAssRecordService.insertObj(this.getLoginUserId(), map);

    }

    /**
     * 初级评委部门评价展示
     *
     * @Param: []
     * @return: java.util.Map
     * @Date: 2019/11/6
     */
    @PostMapping("/api/sectoral/primaryShow")
    @LogPub("初级评委部门评价展示")
    public Map getAllMessages() {
        return deptAssRecordService.getAllMessages(this.getLoginUserId());
    }

    /**
     * 初级评委部门评价提交
     *
     * @Param: [data]
     * @return: void
     * @Date: 2019/11/6
     */
    @LogPub("初级评委部门评价提交")
    @PostMapping("/api/sectoral/primaryEvaluation")
    public void primaryEvaluation(@RequestBody String data) {
        logger.info("data数据为:" + data);
        deptAssRecordService.primaryEvaluation(this.getLoginUserId(), data);
    }

    /**
     * 二级部门评价展示
     *
     * @Param:
     * @return:
     * @Date: 2019/11/6
     */
    @LogPub("二级部门评价展示")
    @PostMapping("/api/sectoral/twoLevelEvaluation")
    public Map twoLevelEvaluation() {
        return deptAssRecordService.twoLevelEvaluation(this.getLoginUserId());
    }

    /**
     * 二级评价提交
     *
     * @Param:
     * @return:
     * @Date: 2019/11/6
     */
    @LogPub("二级评价提交")
    @PostMapping("/api/sectoral/SecondaryToSubmit")
    public void secondaryToSubmit(@RequestBody String data) {
        logger.info("data::::" + data);
        deptAssRecordService.secondaryToSubmit(this.getLoginUserId(), data);
    }

    /**
     * 顶级部门评价展示
     *
     * @Param:
     * @return:
     * @Date: 2019/11/6
     */
    @LogPub("顶级部门评价展示")
    @PostMapping("/api/sectoral/topLevel")
    public Map topLevelEvaluation() {
        return deptAssRecordService.topLevel(this.getLoginUserId());
    }
    /**
     * 测算
     *
     * @Param: [data]
     * @return: java.util.Map
     * @Date: 2019/11/7
     */
    @LogPub("测算")
    @PostMapping("/api/sectoral/topEvaluation")
    public Map topEvaluation(@RequestBody String data) {
        return deptAssRecordService.topEvaluation(this.getLoginUserId(), data);
    }

    /**
     * 顶级部门评价提交
     *
     * @Param: [data]
     * @return: void
     * @Date: 2019/11/7
     */
    @LogPub("顶级部门评价提交")
    @PostMapping("/api/sectoral/finalToSubmit")
    public void finalToSubmit(@RequestBody String data) {
        logger.info("data::::" + data);
        deptAssRecordService.finalToSubmit(this.getLoginUserId(), data);
    }

    /**
     * 部门得分记录
     *
     * @Param: [assessReportInfo]
     * @return: com.github.pagehelper.PageInfo<com.bestvike.gjjjx.data.AssessReportInfo>
     * @Date: 2019/11/9
     */
    @LogPub("部门得分记录")
    @GetMapping("/api/sectoral/fetchSelfDept")
    public List<AssessReportInfo> fetchSelfDept(AssessReportInfo assessReportInfo) {
        return deptAssRecordService.fetchSelfDept(assessReportInfo, this.getLoginUserId());
    }

    @LogPub("评价进度")
    @GetMapping("/api/sectoral/evaluationProgress")
    public List<SysUser>  evaluationProgress() {
        return deptAssRecordService.evaluationProgress(this.getLoginUserId());
    }

}
