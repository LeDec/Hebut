package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.GetDungeonRequest;
import com.android.quest.project.entity.request.JoinDungeonRequest;
import com.android.quest.project.entity.response.GetDungeonResponse;
import com.android.quest.project.service.IRUserDungeonTableService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@CrossOrigin
@RestController
@RequestMapping("/project/r-user-dungeon-table")
public class RUserDungeonTableController {

    @Resource
    IRUserDungeonTableService irUserDungeonTableService;


    @PostMapping(value = "/getApplyDungeon")
    @ApiOperation(value = "获取所有可参加副本", tags = "副本功能")
    public ApiResponse<GetDungeonResponse> getApplyDungeon(
            @RequestBody GetDungeonRequest getDungeonRequest
    ) {
        GetDungeonResponse getDungeonResponse = irUserDungeonTableService.getApplyDungeon(getDungeonRequest);
        return ApiResponse.success(getDungeonResponse);
    }

    @PostMapping(value = "/joinDungeon")
    @ApiOperation(value = "参加副本", tags = "副本功能")
    public ApiResponse joinDungeon(
            @RequestBody JoinDungeonRequest joinDungeonRequest
    ) {
        irUserDungeonTableService.joinDungeon(joinDungeonRequest);
        return ApiResponse.success();
    }

}
