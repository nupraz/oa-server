package com.graduation.oa.controller;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.FileUtils;
import com.graduation.oa.config.annotation.LogPub;
import com.graduation.oa.data.EmpInfo;
import com.graduation.oa.service.EmpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmpInfoController extends BaseController {

    @Value("${app.file.template-path:}")
    protected String templatePath;

    @Autowired
    private EmpInfoService empInfoService;

    @GetMapping("/api/employees/deptId")
    @LogPub("查询所有在职员工信息")
    public List<EmpInfo> fetchAllEmp() {
        return empInfoService.fetchAllEmp();
    }

    @PostMapping("/api/employees/notInUser")
    @LogPub("通过部门编号查询员工列表")
    public List<EmpInfo> fetchEmpNotInUser(@RequestBody EmpInfo empInfo) {
        return empInfoService.fetchEmpNotInUser(empInfo.getDeptId());
    }

    @GetMapping("/api/deptEmployees")
    @LogPub("员工评价页面查询")
    public List<EmpInfo> fetchDeptEmp(EmpInfo empInfo) {
        return empInfoService.fetchDeptEmp(empInfo,this.getLoginUser().getCode());
    }

    @GetMapping("/api/employees")
    @LogPub("员工管理页面查询")
    public List<EmpInfo> fetch(EmpInfo empInfo) {
        return empInfoService.fetch(empInfo);
    }

    /**
     * 新增员工信息
     * @param empInfo
     * @return
     */
    @PostMapping("/api/employees")
    public int create(@RequestBody EmpInfo empInfo) {
        return empInfoService.create(empInfo,this.getLoginUser().getCode());
    }

    /**
     * 修改员工信息
     * @param empInfo
     * @return
     */
    @PutMapping("/api/employees")
    public int modify(@RequestBody EmpInfo empInfo) {
        return empInfoService.modify(empInfo);
    }

    /**
     * 删除员工信息
     * @param ids
     */
    @DeleteMapping("/api/employees/{ids}")
    public void remove(@PathVariable String ids) {
        empInfoService.remove(ids);
    }

    /**
     * 下载员工信息导入模板
     * @param request
     * @param response
     * @param param
     * @throws IOException
     */
    @PostMapping("/api/employees/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response,
                                 @RequestBody Map<String, String> param) throws IOException {
        String fileName = param.get("fileName");
        FileUtils.download(request, response,
                FileUtils.getInputStream(templatePath + File.separator + fileName), "员工导入模板.xlsx");
    }

    /**
     * 员工信息导入
     * 2019/9/17
     * @param file
     * @param fileSource
     * @param path
     * @return
     */
    @PostMapping("/api/employees/import")
    public  Map<String,String> importEmpInfo(@RequestParam("file")MultipartFile[] file,
                                          @RequestParam("fileSource") String[] fileSource,
                                          @RequestParam("path") String path){
        Map<String,String> result=new HashMap<>();
        String fileName = file[0].getOriginalFilename();
        if(!fileName.endsWith("xls")&&!fileName.endsWith("xlsx")){
            logger.info("员工信息文件上传类型不是xls或xlxs");
            System.out.print("员工信息文件上传类型不是xls或xlxs");
            result.put("retCode","");
            result.put("level","");
            result.put("retMsg","员工信息文件上传类型不是xls或xlxs");
            return result;
        }
        String manageUser = this.getLoginUserId();
        try {
            result = empInfoService.importEmpInfo(file[0], fileSource[0], path, manageUser);
        } catch (ServiceException e){
            logger.info("员工信息文件上传发生异常",e);
            result.put("retCode",e.getCode());
            result.put("level", "error");
            result.put("retMsg","导入模板格式存在问题，请使用正确的模板导入");
        }
        return result;
    }
}
