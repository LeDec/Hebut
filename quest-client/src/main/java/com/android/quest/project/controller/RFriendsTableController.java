package com.android.quest.project.controller;


import com.android.quest.common.ApiResponse;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.request.GetFriendsRequest;
import com.android.quest.project.entity.response.GetAchievementResponse;
import com.android.quest.project.entity.response.GetFriendsResponse;
import com.android.quest.project.service.IRFriendsTableService;
import com.android.quest.project.service.IRUserAchievementTableService;
import com.android.quest.project.service.impl.RFriendsTableServiceImpl;
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
@RequestMapping("/project/r-friends-table")
@CrossOrigin
public class RFriendsTableController {
    @Resource
    IRFriendsTableService irFriendsTableService;

    @PostMapping(value = "/getFriends")
    @ApiOperation(value = "查看好友列表", tags = "社区功能")
    public ApiResponse<GetFriendsResponse> getFriends(
            @RequestBody GetFriendsRequest getFriendsRequest
    ) {
        GetFriendsResponse getFriendsResponse = irFriendsTableService.getFriends(getFriendsRequest);
        return ApiResponse.success(getFriendsResponse);
    }
}
