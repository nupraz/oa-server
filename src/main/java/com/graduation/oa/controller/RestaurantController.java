package com.graduation.oa.controller;

import com.graduation.oa.service.RestaurantService;
import com.graduation.oa.data.empOrderEntity.RestMenuInfo;
import com.graduation.oa.data.empOrderEntity.RestaurantInfo;
import com.graduation.oa.data.empOrderEntity.StartOrderInfo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 流程配置
 */
@Api(description = "餐厅管理")
@RestController
@RequestMapping("/api")
public class RestaurantController extends BaseController {
    @Autowired
    private RestaurantService restaurantService;

    /**
     * 查询餐厅信息
     * @param fuzzy
     * @param restName
     * @param sort
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "查询餐厅信息", notes = "查询餐厅信息")
    @GetMapping("/restaurantInfo")
    public PageInfo<RestaurantInfo> getRestInfo(@RequestParam(value = "fuzzy", required = false) String fuzzy,
                                                    @RequestParam(value = "restName",required = false) String restName,
                                                    @RequestParam(value = "sort", required = false) String sort,
                                                    @RequestParam(value = "page", required = false) Integer page,
                                                    @RequestParam(value = "limit", required = false) Integer limit) {
        return restaurantService.getRestInfo(fuzzy, restName,sort, page, limit);

    }
    /**
     * 新增餐厅信息
     * @param restaurantInfo
     * @return
     */
    @ApiOperation(value = "新增餐厅信息", notes = "新增餐厅信息")
    @PostMapping("/restaurantInfo")
    public Map<String,Object> createRestInfo(@RequestBody RestaurantInfo restaurantInfo){
        return restaurantService.createRestInfo(restaurantInfo,getLoginUser().getName());
    }

    /**
     * 修改餐厅信息
     * @param restaurantInfo
     * @return
     */
    @ApiOperation(value = "修改餐厅信息", notes = "修改餐厅信息")
    @PutMapping("/restaurantInfo")
    public Map<String,Object> modifyRestInfo(@RequestBody RestaurantInfo restaurantInfo) {
        return restaurantService.modifyRestInfo(restaurantInfo,getLoginUser().getName());
    }

    /**
     * 删除餐厅信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除餐厅信息", notes = "删除餐厅信息")
    @DeleteMapping("/restaurantInfo/{ids}")
    public Map<String,Object> deleteRestInfo(@PathVariable String ids) {
        return restaurantService.deleteRestInfo(ids,getLoginUser().getName());
    }

    /**
     * 查询菜单信息
     * @param fuzzy
     * @param restId
     * @param menuName
     * @param sort
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "查询菜单", notes = "查询菜单")
    @GetMapping("/menuInfo")
    public PageInfo<RestMenuInfo> fetchMenuInfo (
            @RequestParam(value = "fuzzy", required = false) String fuzzy,
            @RequestParam(value = "restId", required = false) String restId,
            @RequestParam(value = "menuName",required = false) String menuName,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit){
        return restaurantService.fetchMenuInfo(fuzzy, restId, menuName, sort, page, limit);
    }

    /**
     * 新增菜单信息
     * @param restMenuInfo
     * @return
     */
    @ApiOperation(value = "新增菜单信息", notes = "新增菜单信息")
    @PostMapping("/menuInfo")
    public Map<String,Object> createMenuInfo(@RequestBody RestMenuInfo restMenuInfo){
        return restaurantService.createMenuInfo(restMenuInfo,getLoginUser().getName());
    }

    /**
     * 修改菜单信息
     * @param restMenuInfo
     * @return
     */
    @ApiOperation(value = "修改菜单信息", notes = "修改菜单信息")
    @PutMapping("/menuInfo")
    public Map<String,Object> modifyMenuInfo(@RequestBody RestMenuInfo restMenuInfo) {
        return restaurantService.modifyMenuInfo(restMenuInfo,getLoginUser().getName());
    }

    /**
     * 删除菜单信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除菜单信息", notes = "删除菜单信息")
    @DeleteMapping("/menuInfo/{ids}")
    public Map<String,Object> deleteMenuInfo(@PathVariable String ids) {
        return restaurantService.deleteMenuInfo(ids,getLoginUser().getName());
    }

    /**
     * 新增发起订餐信息
     * @param startOrderInfo
     * @return
     */
    @ApiOperation(value = "新增发起订餐信息", notes = "新增发起订餐信息")
    @PostMapping("/orderInfo")
    public Map<String,Object> creatOrderInfo(@RequestBody StartOrderInfo startOrderInfo){
        return restaurantService.createOrderInfo(startOrderInfo,getLoginUser().getName());
    }
    /**
     * 取消订餐
     * @return
     */
    @ApiOperation(value = "取消订餐", notes = "取消订餐")
    @DeleteMapping("/orderInfo")
    public Map<String,Object> deleteOrderInfo() {
        return restaurantService.deleteOrderInfo(getLoginUser().getName());
    }

    /**
     * 查询是否正在订餐
     * @return
     */
    @ApiOperation(value = "查询是否正在订餐", notes = "查询是否正在订餐")
    @GetMapping("/isHaveOrder")
    public Map<String,Object> getIsHaveOrder() {
        return restaurantService.getIsHaveOrder();
    }

    /**
     * 获取所有预设订餐信息
     * @return
     */
    @ApiOperation(value = "获取所有预设订餐信息", notes = "获取所有预设订餐信息")
    @GetMapping("/startOrderInfo")
    public PageInfo<StartOrderInfo> getAllPresetTime(@RequestParam(value = "fuzzy", required = false) String fuzzy,
                                                @RequestParam(value = "page", required = false) Integer page,
                                                @RequestParam(value = "limit", required = false) Integer limit) {
        return restaurantService.getAllPresetTime(fuzzy, page, limit);
    }

    /**
     * 删除所选预设订餐信息
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除预设订餐信息", notes = "删除预设订餐信息")
    @DeleteMapping("/startOrderInfo/{ids}")
    public Map<String,Object> deletePresetTimeInfo(@PathVariable String ids) {
        return restaurantService.deletePresetTimeInfo(ids, getLoginUser().getName());
    }

}
