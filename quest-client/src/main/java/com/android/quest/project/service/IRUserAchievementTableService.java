package com.android.quest.project.service;

import com.android.quest.project.entity.RUserAchievementTable;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.GetAchievementResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface IRUserAchievementTableService extends IService<RUserAchievementTable> {

    GetAchievementResponse getAchievementList(CheckRequest checkRequest);
}
