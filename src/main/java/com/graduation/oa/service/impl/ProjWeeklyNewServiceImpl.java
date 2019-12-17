package com.graduation.oa.service.impl;

import cn.hutool.json.JSONUtil;
import com.bestvike.commons.utils.EncryptUtils;
import com.bestvike.commons.utils.ExcelUtils;
import com.bestvike.commons.utils.FileUtils;
import com.graduation.oa.dao.LogFileDao;
import com.graduation.oa.dao.EmpInfoDao;
import com.graduation.oa.dao.ProjWeeklyNewDao;
import com.graduation.oa.data.*;
import com.graduation.oa.data.projEntity.*;
import com.graduation.oa.service.BaseService;
import com.graduation.oa.service.ProjWeeklyNewService;
import com.graduation.oa.common.util.excelUtil.ExcelWriteUtil;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class ProjWeeklyNewServiceImpl extends BaseService implements ProjWeeklyNewService {


    @Autowired
    private LogFileDao logFileDao;
    @Autowired
    private ProjWeeklyNewDao projWeeklyNewDao;
    @Autowired
    private EmpInfoDao empInfoDao;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;
    @Value("${app.file.uploadPath}")
    private String uploadPath;
    @Value("${app.file.template-path:}")
    protected String templatePath;

    /**
     * 项目周报分页查询
     *
     * @param fuzzy
     * @param projId
     * @param projName
     * @param projManage
     * @param empName
     * @param sort
     * @param page
     * @param limit
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public PageInfo<ProjWeeklyNew> selectProWeekly(String fuzzy, String projId, String projName, String projManage, String empName, String dateWeek, String sort, int page, int limit) {
        PageInfo<ProjWeeklyNew> projWeeklyNewPageInfo = PageHelper.startPage(page, limit).doSelectPageInfo(new ISelect() {
            @Override
            public void doSelect() {
                String column = "";
                String sortTemp = "";
                if (sort != null && !"".equals(sort)) {
                    String[] arr = sort.split(",");
                    column = arr[0];
                    sortTemp = arr[1];
                }
                String dateWeekTemp = dateWeek;
                if (dateWeek != null && !"".equals(dateWeek)) {
                    dateWeekTemp = dateWeek.substring(0, 10);
                }
                List<ProjWeeklyNew> list = projWeeklyNewDao.getProjWeeklyNewInfo(fuzzy, projId, projName, projManage, empName, dateWeekTemp, sortTemp, column);
                for(ProjWeeklyNew projWeeklyNew:list){
                    Calendar calendar = Calendar.getInstance();
                    try {
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(projWeeklyNew.getStartDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    projWeeklyNew.setDateWeek("第" + String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)) + "周");
                }

            }
        });
        return projWeeklyNewPageInfo;
    }

    /**
     * 导入项目周报
     *
     * @param file
     * @param fileSource
     * @param path
     * @param manageUser
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> importProWeekly(MultipartFile file, String fileSource, String path, String manageUser) {
        logger.info("上传项目周报文件开始");
        Map<String, String> resultMap = new HashMap<>();

        //上传文件  读取文件内容
        InputStream inputStream = null;
        MultipartFile multipartFile = file;
        String tempName = fileSource;
        String fileName = multipartFile.getOriginalFilename();
        logger.info("upload filename: " + tempName + "-" + fileName);
        if (fileName == null || fileName.trim().equals("")) {
            logger.info("项目周报文件上传文件名为空");
            resultMap.put("retCode", "U1");
            resultMap.put("level", "error");
            resultMap.put("retMsg", "文件上传文件名为空");
            logger.info("importTravel end 项目周报文件上传文件名为空");
            return resultMap;
        }
        try {
            inputStream = multipartFile.getInputStream();
            logger.info("获取上传文件流");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("importTravel end 获取文件流失败");
            resultMap.put("retCode", "U1");
            resultMap.put("level", "error");
            resultMap.put("retMsg", "获取输入流失败");
            return resultMap;
        }
        logger.info("开始进行文件上传");
        byte[] bytes = null;
        try {
            bytes = FileUtils.read(inputStream);
        } catch (Exception e) {
            logger.error("上传文件读取文件流异常", e);
            resultMap.put("retCode", "U1");
            resultMap.put("level", "error");
            resultMap.put("retMsg", "上传文件读取文件流异常");
            logger.info("importTravel end 上传文件读取文件流异常");
            return resultMap;
        }
        int fileSize = bytes.length;
        long maxSize = 0L;
        if (!com.bestvike.commons.utils.StringUtils.isEmpty(this.maxFileSize)) {
            this.maxFileSize = this.maxFileSize.toUpperCase();
            maxSize = this.maxFileSize.endsWith("KB")
                    ? Long.valueOf(this.maxFileSize.substring(0, this.maxFileSize.length() - 2))
                    .longValue() * 1024L
                    : (this.maxFileSize.endsWith("MB")
                    ? Long.valueOf(this.maxFileSize.substring(0, this.maxFileSize.length() - 2))
                    .longValue() * 1024L * 1024L
                    : Long.valueOf(this.maxFileSize).longValue());
        }

        if (maxSize > 0L && (long) fileSize > maxSize) {
            Object bytes1 = null;
            resultMap.put("retCode", "U1");
            resultMap.put("level", "error");
            resultMap.put("retMsg", "文件大小不能超过" + this.maxFileSize);
            logger.info("上传操作文件大小不能超过" + this.maxFileSize);
            return resultMap;
        }

        String md5 = DigestUtils.md5Hex(bytes);
        String sha1 = DigestUtils.sha1Hex(bytes);

        LogFile param = new LogFile();
        param.setFileMd5(md5);
        String sign = md5;
        String extName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String filePath = FileUtils.filePath(uploadPath + path);
        String upFileName = com.bestvike.commons.utils.StringUtils.guid() + "." + extName;
        try {
            FileUtils.save(bytes, new FileOutputStream(filePath + upFileName));
        } catch (Exception e) {
            resultMap.put("retCode", "U1");
            resultMap.put("level", "error");
            resultMap.put("retMsg", "上传文件异常");
            logger.info("项目周报文件上传异常", e);
            return resultMap;
        }
        LogFile logFile = new LogFile();
        logFile.setFileId(com.bestvike.commons.utils.StringUtils.guid());
        logFile.setFileType(null);
        logFile.setSubType(null);
        logFile.setFileName(upFileName);
        logFile.setOriginName(fileName);
        logFile.setFilePath(filePath);
        logFile.setExtName(extName);
        logFile.setFileSize(Integer.valueOf(fileSize));
        logFile.setFileMd5(md5);
        logFile.setFileSha1(sha1);
        logFile.setFileSign(sign);
        logFile.setFileSource(tempName);
        logFile.setFileState("0000");
        logFile.setManageTime(new Date());
        logFile.setManageUser(manageUser);
        this.logFileDao.insert(logFile);
        logger.info("项目周报文件上传完成");

        String msg = "";
        Map<String, Object> varMap = new HashMap<>();
        //周报信息主体
        ProjWeeklyNew projWeeklyNew = new ProjWeeklyNew();
        varMap.put("projWeeklyNew", projWeeklyNew);
        //项目周报详情
        List<ProjWeeklyNewDetail> projWeeklyNewDetail = new ArrayList<>();
        varMap.put("projWeeklyNewDetail", projWeeklyNewDetail);
        //项目周报风险
        List<ProjWeeklyNewRisk> projWeeklyNewRisk = new ArrayList<>();
        varMap.put("projWeeklyNewRisk", projWeeklyNewRisk);
        //下周项目周报
        List<ProjWeeklyNewNext> projWeeklyNewNext = new ArrayList<>();
        varMap.put("projWeeklyNewNext", projWeeklyNewNext);
        try {
            FileInputStream is = new FileInputStream(logFile.getFilePath() + logFile.getFileName());
            ExcelUtils.importFile(templatePath, "projWeeklyNewImport.xml", is, varMap);
        } catch (Exception e) {
            logger.info("项目周报信息转换失败：" + e);
            resultMap.put("level", "warning");
            resultMap.put("retMsg", "请使用模板导入");
            ExcelWriteUtil.deleteFileDirectory(logFile.getFilePath() + logFile.getFileName());
            return resultMap;
        }
        if (projWeeklyNew == null) {
            resultMap.put("retCode", "9999");
            resultMap.put("level", "warning");
            resultMap.put("retMsg", "导入数据为空");
            ExcelWriteUtil.deleteFileDirectory(logFile.getFilePath() + logFile.getFileName());
            return resultMap;
        }

        try {
            Map<String, Object> temp = checkProjWeeklyNewMap(projWeeklyNewDetail, projWeeklyNewRisk, projWeeklyNewNext);
            //获取分支数据
            Map<String, Object> content = new HashMap<>();
            content.put("details", temp.get("details"));
            content.put("risks", temp.get("risks"));
            content.put("nexts", temp.get("nexts"));
            //处理总数据
            Map<String, Object> checkMap = checkProjWeeklyNewData(projWeeklyNew, content, manageUser);
            if (checkMap.get("msg").toString().length() == 0) {
                ProjWeeklyNew resultProj = (ProjWeeklyNew) checkMap.get("projWeeklyNew");
                ProjWeeklyNew proj = projWeeklyNewDao.selectByPrimaryKey(resultProj);
                if (proj != null) {
                    //更新周报数据
                    projWeeklyNewDao.updateByPrimaryKey(resultProj);
                } else {
                    //插入周报数据
                    projWeeklyNewDao.insert(resultProj);
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                msg = checkMap.get("msg").toString();
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.info("信息导入失败:" + e);
            resultMap.put("level", "warning");
            resultMap.put("retMsg", "导入失败！");
            return resultMap;
        } finally {
            ExcelWriteUtil.deleteFileDirectory(logFile.getFilePath() + logFile.getFileName());
        }

        if (msg.length() > 0) {
            resultMap.put("retCode", "9999");
            resultMap.put("level", "warning");
            resultMap.put("retMsg", msg);
        } else {
            resultMap.put("retCode", "0000");
            resultMap.put("level", "success");
            resultMap.put("retMsg", "上传成功");
        }
        return resultMap;
    }

    /**
     * 导出项目周报
     *
     * @param params
     * @return
     */
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ProjWeeklyNew> exportProjWeeklyData(Map<String, String> params) {
        String fuzzy = params.get("fuzzy");
        String projId = params.get("projId");
        String projName = params.get("projName");
        String projManage = params.get("projManage");
        String empName = params.get("empName");
        String dateWeekTemp = params.get("dateWeekTemp");
        String column = "";
        String sortTemp = "ascending";
        List<ProjWeeklyNew> list = projWeeklyNewDao.getProjWeeklyNewInfo(fuzzy, projId, projName, projManage, empName, dateWeekTemp, sortTemp, column);
        return list;
    }

    /**
     * 处理总数据
     *
     * @param projWeeklyNew
     * @param tempMap
     * @param manageUser
     * @return
     */
    private Map<String, Object> checkProjWeeklyNewData(ProjWeeklyNew projWeeklyNew, Map tempMap, String manageUser) throws ParseException {
        Map<String, Object> resultMap = new HashMap<>();
        StringBuffer msgbuf = new StringBuffer();
        String msg = "";
        String content = JSONUtil.toJsonStr(tempMap);
        //项目编号
        String projId = projWeeklyNew.getProjId();
        //项目名称
        String projName = projWeeklyNew.getProjName();
        //开始日期
        String startDate = projWeeklyNew.getStartDate();
        //结束日期
        String endDate = projWeeklyNew.getEndDate();
        //项目经理
        String projManage = projWeeklyNew.getProjManage();
        //报告人
        String empName = projWeeklyNew.getEmpName();
        //判断信息规范
        if( projId == null || "".equals(projId)) {
            msgbuf.append("关键信息不完善，项目编号为空；");
        }/* else {
            List<ProjInfo> list1 = projWeeklyNewDao.fetchProjInfo(projId,"id");
            List<ProjInfo> list2 = projWeeklyNewDao.fetchProjInfo(projName,"name");
            if(list1 == null || list1.size()==0){
                if(list2 == null || list2.size()==0){
                    msgbuf.append("项目编号、名称不存在；");
                } else {
                    //自动纠正id
                    projWeeklyNew.setProjId(list2.get(0).getProjId());
                }
            } else {
                //自动纠正name
                projWeeklyNew.setProjName(list1.get(0).getProjName());
            }
        }*/
        if( startDate == null || "".equals(startDate) ) { msgbuf.append("关键信息不完善，开始日期为空；"); }
        if( endDate == null || "".equals(endDate) ) { msgbuf.append("关键信息不完善，结束日期为空；"); }
        if (startDate != null && !"".equals(startDate) && endDate != null && !"".equals(endDate)){
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String s1 = dateFormat.format(DateUtil.getJavaDate(Double.parseDouble(startDate)));
            String s2 = dateFormat.format(DateUtil.getJavaDate(Double.parseDouble(endDate)));
            Map<String, String> getFirstMonday = getWeekDate(s1);
            Map<String, String> getSecondMonday = getWeekDate(s2);
            String firstMonday = getFirstMonday.get("mondayDate");
            String secondMonday = getSecondMonday.get("mondayDate");
            if (!firstMonday.equals(secondMonday)){
                msgbuf.append("开始结束日期不在同一周；");
            }
            if(!getFirstMonday.get("mondayDate").equals(s1)){
                msgbuf.append("开始日期错误，请设置为周一开始；");
            }
            projWeeklyNew.setStartDate(s1);
            projWeeklyNew.setEndDate(s2);
        }
        if( projManage == null || "".equals(projManage) ) {
            msgbuf.append("关键信息不完善，项目经理为空；");
        } else {
            Example projManageExample = new Example(SysEmp.class);
            projManageExample.createCriteria().andEqualTo("empName", projManage);
            //可能有重名员工，故用list
            List<EmpInfo> manageEmp = empInfoDao.selectByExample(projManageExample);
            if (manageEmp == null || manageEmp.size()==0) {
                msgbuf.append("项目经理错误，不存在当前项目经理，请修改后重新导入；");
            }
        }
        if( empName == null || "".equals(empName) ) {
            msgbuf.append("关键信息不完善，报告人为空；");
        } else {
            Example empExample = new Example(SysEmp.class);
            empExample.createCriteria().andEqualTo("empName", empName);
            //可能有重名员工，故用list
            List<EmpInfo> emp = empInfoDao.selectByExample(empExample);
            if (emp == null || emp.size()==0) {
                msgbuf.append("报告人错误，不存在当前员工，请修改后重新导入；");
            }
        }
        if ((projId != null && !"".equals(projId)) && (empName != null && !"".equals(empName)) && (!"".equals(startDate) && startDate != null)) {
            try {
                projWeeklyNew.setPwId(EncryptUtils.MD5Encode(projId + empName + startDate));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        projWeeklyNew.setContentDetail(content);
        projWeeklyNew.setManageUser(manageUser);
        projWeeklyNew.setManageTime(new Date());
        projWeeklyNew.setPwState("0000");
        if (msgbuf.length() > 0) {
            msg = msg + msgbuf.toString();
        }
        if (msgbuf.length() == 0) {
            resultMap.put("projWeeklyNew", projWeeklyNew);
        }
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * 处理分支数据
     *
     * @param projWeeklyNewDetails
     * @param projWeeklyNewRisks
     * @param projWeeklyNewNexts
     * @return
     */
    private Map<String, Object> checkProjWeeklyNewMap(List<ProjWeeklyNewDetail> projWeeklyNewDetails, List<ProjWeeklyNewRisk> projWeeklyNewRisks, List<ProjWeeklyNewNext> projWeeklyNewNexts) {
        Map<String, Object> resultMap = new HashMap<>();
        StringBuffer gitMsg = new StringBuffer();
        List<ProjWeeklyNewDetail> details = new ArrayList<>();
        List<ProjWeeklyNewRisk> risks = new ArrayList<>();
        List<ProjWeeklyNewNext> nexts = new ArrayList<>();
        int detailNum = 1;
        for (ProjWeeklyNewDetail detail : projWeeklyNewDetails) {
            try {
                detail.setWorkNum(Integer.toString(detailNum));
                details.add(detail);
                detailNum++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int riskNum = 0;
        for (ProjWeeklyNewRisk risk : projWeeklyNewRisks) {
            //第一次跳过
            if(riskNum==0){
                riskNum++;
                continue;
            }
            risk.setNum(Integer.toString(riskNum));
            risks.add(risk);
            riskNum++;
        }
        int nextNum = 0;
        for (ProjWeeklyNewNext next : projWeeklyNewNexts) {
            //第一次跳过
            if(nextNum==0){
                nextNum++;
                continue;
            }
            String partner = next.getPartner();
            String detail = next.getDetail();
            String schedule = next.getSchedule();
            if(partner == null && detail==null && schedule==null){
                next.setDetail(next.getTheme());
                next.setTheme("");
            }
            next.setNum(Integer.toString(nextNum));
            nexts.add(next);
            nextNum++;
        }
        resultMap.put("details", details);
        resultMap.put("risks", risks);
        resultMap.put("nexts", nexts);
        resultMap.put("gitMsg", gitMsg);
        return resultMap;
    }

    /**
     * 判断是否为整数
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 处理错误信息提示
     *
     * @param str
     * @param index
     * @param msgStr
     * @return
     */
    private StringBuffer checkMsg(StringBuffer str, int index, String msgStr) {
        if (str == null || str.length() == 0) {
            str.append("第" + index + "行,数据错误," + msgStr);
        } else {
            str.append("," + msgStr);
        }
        return str;
    }

    /**
     * 删除项目周报信息
     *
     * @param deleteId
     * @return
     */
    public Map deleteProjWeeklyNew(String deleteId) {
        Map<String, String> resultMap = new HashMap<>();
        String msg = "";
        Example example = new Example(ProjWeeklyNew.class);
        example.createCriteria().andEqualTo("pwId", deleteId);
        try {
            ProjWeeklyNew projWeeklyNew = projWeeklyNewDao.selectOneByExample(example);
            projWeeklyNew.setPwState("9999");
            projWeeklyNewDao.updateByPrimaryKey(projWeeklyNew);
        } catch (Exception e) {
            resultMap.put("retCode", "9999");
            resultMap.put("level", "error");
            resultMap.put("msg", "删除失败");
        }
        resultMap.put("retCode", "0000");
        resultMap.put("level", "success");
        resultMap.put("msg", "删除成功");
        return resultMap;
    }

    /**
     * 计算某个时间的周一到周日
     *
     * @map
     */
    public static Map<String, String> getWeekDate(String nowDate) throws ParseException {
        Log logger = LogFactory.getLog(nowDate);
        Map<String, String> map = new HashMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(nowDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        logger.info("需要计算的日期:" + sdf.format(cal.getTime()));
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        String weekBegin = sdf.format(mondayDate);
        logger.info("所在周星期一的日期：" + weekBegin);
        cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        String weekEnd = sdf.format(sundayDate);
        logger.info("所在周星期日的日期：" + weekEnd);
        map.put("mondayDate", weekBegin);
        map.put("sundayDate", weekEnd);
        return map;
    }
}
