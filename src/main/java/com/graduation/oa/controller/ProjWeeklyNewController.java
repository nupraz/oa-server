package com.graduation.oa.controller;

import com.bestvike.commons.exception.ServiceException;
import com.bestvike.commons.utils.FileUtils;
import com.graduation.oa.data.projEntity.ProjWeeklyNew;
import com.graduation.oa.service.ProjWeeklyNewService;
import com.graduation.oa.common.util.excelUtil.ExcelWriteUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
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

/**
 * 季度自评/项目周报
 */
@RestController
public class ProjWeeklyNewController extends BaseController {

	@Value("${app.file.exportPath:}")
	private String exportPath;

	@Autowired
	private ProjWeeklyNewService projWeeklyNewService;

	@Value("${app.file.template-path:}")
	protected String templatePath;

	/**
	 * 项目周报查询
	 *
	 * @param page
	 * @param limit
	 * @param fuzzy
	 * @param sort
	 */
	@GetMapping("/api/projWeeklyNew/fetchProWeekly")
	public PageInfo<ProjWeeklyNew> selectProWeekly(
			@RequestParam(value = "fuzzy", required = false) String fuzzy,
			@RequestParam(value = "projId", required = false) String projId,
			@RequestParam(value = "projName", required = false) String projName,
			@RequestParam(value = "projManage", required = false) String projManage,
			@RequestParam(value = "empName", required = false) String empName,
			@RequestParam(value = "dateWeek", required = false) String dateWeek,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "limit", required = false) int limit) {
		return projWeeklyNewService.selectProWeekly(fuzzy, projId, projName, projManage, empName, dateWeek, sort, page, limit);
	}
	/**
	 * 项目周报导入
	 *
	 * @param file
	 * @param fileSource
	 * @param path
	 * @return
	 */
	@ApiOperation(value = "项目周报导入", notes = "项目周报导入")
	@PostMapping("/api/projWeeklyNew/importProjWeekly")
	public Map<String, String> importProWeekly(@RequestParam("file") MultipartFile[] file,
											   @RequestParam("fileSource") String[] fileSource,
											   @RequestParam("path") String path) {
		Map<String, String> result = new HashMap<>();
		String fileName = file[0].getOriginalFilename();
		if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
			logger.info("项目周报文件上传类型不是xls或xlxs");
			System.out.print("项目周报文件上传类型不是xls或xlxs");
			result.put("retCode", "");
			result.put("level", "");
			result.put("retMsg", "项目周报文件上传类型不是xls或xlxs");
			return result;
		}
		String manageUser = getLoginUser().getCode();
		try {
			result = projWeeklyNewService.importProWeekly(file[0], fileSource[0], path, manageUser);
		} catch (ServiceException e) {
			logger.info("项目周报文件上传发生异常", e);
			result.put("retCode", e.getCode());
			result.put("level", "error");
			result.put("retMsg", "导入模板格式存在问题，请使用正确的模板导入");
		}
		return result;
	}

	/**
	 * 模板下载功能
	 * @param request
	 * @param response
	 * @param param
	 * @throws IOException
	 */
	@ApiOperation(value = "项目周报导入模板下载", notes = "项目周报导入模板下载")
	@PostMapping("/api/projWeeklyNew/template")
	public void downloadTemplate(HttpServletRequest request, HttpServletResponse response,
								 @RequestBody Map<String, String> param) throws IOException {
		String fileName = param.get("fileName");
		FileUtils.download(request, response,
				FileUtils.getInputStream(templatePath + File.separator + fileName), "项目周报导入模板.xlsx");
	}

	/**
	 * 删除项目周报
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "删除项目周报", notes = "删除项目周报")
	@DeleteMapping("/api/projWeeklyNew/delete/{id}")
	public Map deleteProjWeeklyNew(@PathVariable String id){
		logger.info("周报编号打印："+id);
		return projWeeklyNewService.deleteProjWeeklyNew(id);
	}
	/**
	 *项目周报导出功能
	 * @param param
	 * @return
	 */
	@ApiOperation(value = "导出项目周报", notes = "导出项目周报")
	@PostMapping("/api/projWeeklyNew/exportProjWeekly")
	public Map<String, String> exportProjWeekly(@RequestBody Map<String, Object> param) {

//		Map<String, String> params = (Map<String, String>) param.get("params");
//		List<ProjWeeklyNew> datas = projWeeklyNewService.exportProjWeeklyData(params);
		//导出勾选的数据
		List<Map<String, Object>> datas = (List<Map<String, Object>>) param.get("params");

		String path = exportPath + "/" + getLoginUserId();
		String fileName = "项目周报数据明细.xlsx";
		ExcelWriteUtil.exportProjWeeklyDataToExcel(datas, path, fileName);
		//下载输出
		Map<String, String> map = new HashMap<>();
		map.put("fileName", path + "/" + fileName);
		map.put("pathName", path);
		return map;
	}

}