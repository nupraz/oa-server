package com.graduation.oa.controller;

import com.graduation.oa.data.empOrderEntity.EmpOrderInfo;
import com.graduation.oa.data.empOrderEntity.RestMenuInfo;
import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import com.graduation.oa.service.EmpOrderService;
import com.graduation.oa.common.util.PageInfoSon;
import com.graduation.oa.common.util.excelUtil.ExcelWriteUtil;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "订餐管理")
@RestController
public class EmpOrderController extends BaseController {

    @Value("${app.file.exportPath:}")
    private String exportPath;
    @Autowired
    private EmpOrderService empOrderService;


    /**
     * @description 订餐查询
     * @author     zhengwenshan
     * @date        2019/7/30
     * @param       fuzzy, empName, startDate, endDate, sort, page, limit
     * @return      com.github.pagehelper.PageInfo<com.bestvike.oa.data.empOrderEntity.EmpOrderInfo>
     */
    @ApiOperation(value = "订餐列表", notes = "订餐列表")
    @GetMapping("/api/empOrderInfo")
    public PageInfo<EmpOrderInfo> fetchEmpOrders(
            @RequestParam(value = "fuzzy", required = false) String fuzzy,
            @RequestParam(value = "empName", required = false) String empName,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", required = false) int page,
            @RequestParam(value = "limit", required = false) int limit) {
        //获取用户ID及角色编号
        String empId = getLoginUser().getCode();
        String roleIds = getLoginUser().getRoles();
        //获取用户订单信息
        //信息列表
        PageInfo<EmpOrderInfo> empOrderInfoPageInfo = empOrderService.fetchEmpOrders(fuzzy, empName, startDate, endDate, sort, page, limit,empId,roleIds);
        //列表汇总
        PageInfo<EmpOrderInfo> empOrderInfoPageInfoAll = empOrderService.fetchEmpOrders(fuzzy, empName, startDate, endDate, sort, page,Integer.MAX_VALUE,empId,roleIds);

        return PageInfoSon.convertPageInfo2PageSon(empOrderInfoPageInfo,empOrderInfoPageInfoAll);
    }

    /**
     * @description 新增员工订餐
     * @author     zhengwenshan
     * @date        2019/8/2
     * @param       empOrderInfo
     * @return      java.util.Map<java.lang.String,java.lang.Object>
     */
    @ApiOperation(value = "新增员工订餐", notes = "新增员工订餐")
    @PostMapping("/api/empOrderInfo")
    public Map<String,Object> creatOrderInfo(@RequestBody EmpOrderInfo empOrderInfo){
        return empOrderService.createOrderInfo(empOrderInfo, getLoginUser().getCode());
    }

    /**
     * @description 修改订餐信息
     * @author     zhengwenshan
     * @date        2019/8/2
     * @param       empOrderInfo
     * @return      java.util.Map<java.lang.String,java.lang.Object>
     */
    @ApiOperation(value = "修改订餐信息", notes = "修改订餐信息")
    @PutMapping("/api/empOrderInfo")
    public Map<String,Object> modifyOrderInfo(@RequestBody EmpOrderInfo empOrderInfo){
        return empOrderService.modifyOrderInfo(empOrderInfo, getLoginUser().getName());
    }

    /**
     * @description 删除订餐信息
     * @author     zhengwenshan
     * @date        2019/8/2
     * @param       ids
     * @return      java.util.Map<java.lang.String,java.lang.Object>
     */
    @ApiOperation(value = "删除订餐信息", notes = "删除订餐信息")
    @DeleteMapping("/api/empOrderInfo/{ids}")
    public Map<String,Object> deleteEmpOrderInfo(@PathVariable String ids) {
        return empOrderService.deleteEmpOrderInfo(ids, getLoginUser().getName());
    }

    /**
     * @description 导出订餐信息
     * @author     zhengwenshan
     * @date        2019/8/2
     * @param
     * @return
    */
    @ApiOperation(value = "导出订餐信息", notes = "导出订餐信息")
    @PostMapping("")
    public Map<String, String> exportEmpOrderData(@RequestBody Map<String, Object> param) {

        String headerName = "工号;姓名;菜单;价格";
        String[] header = headerName.split(";");
        String empId = getLoginUser().getCode();
        String roleIds = getLoginUser().getRoles();
        Map<String, String> params = (Map<String, String>) param.get("params");
        List<EmpOrderInfo> orderList = empOrderService.exportEmpOrderData(params, empId, roleIds);
        //填充数据
        List<String[]> datas = new ArrayList<>();
        if (orderList != null && orderList.size() > 0) {
            for (EmpOrderInfo empOrderInfo : orderList) {
                String[] data = new String[4];
                data[0] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getEmpId());
                data[1] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getEmpName());
                data[2] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getMenuName());
                data[3] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getPrice());
                datas.add(data);
            }
        }
        String path = exportPath + "/" + getLoginUserId();
        String fileName = "员工订餐信息.xlsx";
        ExcelWriteUtil.exportTravelDataToExcel(header, datas, path, fileName);
        //下载输出
        Map<String, String> map = new HashMap<>();
        map.put("fileName", path + "/" + fileName);
        map.put("pathName", path);
        return map;
    }

    /**
     * @description 导出订餐汇总信息
     * @author     zhengwenshan
     * @date        2019/8/7
     * @param       param
     * @return
     */
    @ApiOperation(value = "导出订餐汇总信息", notes = "导出订餐汇总信息")
    @PostMapping("/api/empOrderInfo/exportOrdersExcel")
    public Map<String, String> exportEmpOrderStatistics(@RequestBody Map<String, Object> param) {

        String headerName = "日期;餐厅;套餐;数量;单价;总计;";
        String[] header = headerName.split(";");
        String empId = getLoginUser().getCode();
        String roleIds = getLoginUser().getRoles();
        Map<String, String> params = (Map<String, String>) param.get("params");
        List<EmpOrderInfo> orderList = empOrderService.exportEmpOrderStatistics(params);
        //填充数据
        List<String[]> datas = new ArrayList<>();
        if (orderList != null && orderList.size() > 0) {
            for (EmpOrderInfo empOrderInfo : orderList) {
                if(empOrderInfo.getOrderDate() == null && empOrderInfo.getRestName() == null && empOrderInfo.getMenuName() == null){
                    empOrderInfo.setOrderDate("总计");
                }else if(empOrderInfo.getOrderDate() != null && empOrderInfo.getRestName() == null && empOrderInfo.getMenuName() == null){
                    empOrderInfo.setRestName("合计");
                }
                String[] data = new String[6];
                data[0] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getOrderDate());
                data[1] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getRestName());
                data[2] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getMenuName());
                data[3] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getTotalMenu());
                data[4] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getPrice());
                data[5] = ExcelWriteUtil.NullToEmpty(empOrderInfo.getPriceSum());
                datas.add(data);
            }
        }
        String path = exportPath + "/" + getLoginUserId();
        String fileName = "员工订餐信息.xlsx";
        ExcelWriteUtil.exportOrderDataToExcel(header, datas, path, fileName);
        //下载输出
        Map<String, String> map = new HashMap<>();
        map.put("fileName", path + "/" + fileName);
        map.put("pathName", path);
        return map;
    }

    /**
     * 添加按钮及员工下拉列表显示控制
     * @param
     * @return
     */
    @ApiOperation(value = "添加按钮及下拉列表显示控制", notes = "添加按钮及下拉列表显示控制")
    @GetMapping("/api/empOrderInfo/fetchIsShowAddAndEmpOptions")
    public Map<String, Object> fetchIsShowAddAndEmpOptions() {
        return empOrderService.fetchIsShowAddAndEmpOptions(getLoginUser().getCode(), getLoginUser().getRoles());
    }
    /**
     * 获取员工信息
     * @param
     * @return
     */
    @ApiOperation(value = "获取员工信息", notes = "获取员工信息")
    @GetMapping("/api/empOrderInfo/fetchEmpData")
    public Map<String, List> getEmpData(){
        return empOrderService.getEmpData();
    }

    /**
     * 获取所有可用餐厅信息
     * @return
     */
    @ApiOperation(value = "获取所有可用餐厅信息", notes = "获取所有可用餐厅信息")
    @GetMapping("/api/empOrderInfo/restaurants")
    public List<RestaurantInfo> getRestaurantsInUse() {
        return empOrderService.getRestaurantsInUse();
    }

    /**
     * 通过餐厅id 拉取可用菜单
     * @param restId
     * @return
     */
    @ApiOperation(value = "通过餐厅id 拉取可用菜单", notes = "通过餐厅id 拉取可用菜单")
    @GetMapping("/api/empOrderInfo/{id}/menus")
    public List<RestMenuInfo> getMenusInUseByRestId(@PathVariable("id") String restId) {
        return empOrderService.getMenusInUseByRestId(restId);
    }

}
