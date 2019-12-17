package com.graduation.oa.service;

import com.graduation.oa.data.projEntity.ProjWeeklyNew;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface ProjWeeklyNewService {

    /**
     * 分页查询项目周报
     * @param page
     * @param limit
     * @param fuzzy
     * @param projName
     * @param sort
     * @return PageInfo<QuqEvaluateDto>
     */
    PageInfo<ProjWeeklyNew> selectProWeekly(String fuzzy, String projId, String projName, String projManage, String empName, String dateWeek, String sort, int page, int limit);

	/**
	 * 导入项目周报
	 * @param file
	 * @param fileSource
	 * @param path
	 * @param manageUser
	 * @return
	 */
	Map<String,String> importProWeekly(MultipartFile file, String fileSource, String path, String manageUser);

	Map<String,String> deleteProjWeeklyNew(String deleteId);
	/**
	 * 导出项目周报
	 * @param params
	 * @return
	 */
	List<ProjWeeklyNew> exportProjWeeklyData(Map<String, String> params);
}
