package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.request.CheckUserListRequest;
import com.android.quest.project.entity.request.DeleteDungeonRequest;
import com.android.quest.project.entity.response.CheckDungeonListResponse;
import com.android.quest.project.entity.response.CheckUserListResponse;
import com.android.quest.project.service.IDungeonTableService;
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
@RestController
@RequestMapping("/project/dungeon-table")
@CrossOrigin
public class DungeonTableController {

    @Resource
    IDungeonTableService iDungeonTableService;

    @PostMapping(value = "/checkDungeonList")
    @ApiOperation(value = "查看所有副本", tags = "web端接口")
    public ApiResponse<CheckDungeonListResponse> checkDungeonList(
            @RequestBody CheckRequest checkRequest
    ) {
        CheckDungeonListResponse checkUserListResponse = iDungeonTableService.checkDungeonList(checkRequest);
        return ApiResponse.success(checkUserListResponse);
    }

    @PostMapping(value = "/deleteDungeon")
    @ApiOperation(value = "删除副本", tags = "web端接口")
    public ApiResponse deleteDungeon(
            @RequestBody DeleteDungeonRequest deleteDungeonRequest
    ) {
        iDungeonTableService.deleteDungeon(deleteDungeonRequest);
        return ApiResponse.success();
    }

}
