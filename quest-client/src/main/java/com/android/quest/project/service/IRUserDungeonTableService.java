package com.android.quest.project.service;

import com.android.quest.project.entity.RUserDungeonTable;
import com.android.quest.project.entity.request.GetDungeonRequest;
import com.android.quest.project.entity.request.JoinDungeonRequest;
import com.android.quest.project.entity.response.GetDungeonResponse;
import com.android.quest.project.entity.response.GetQuestResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface IRUserDungeonTableService extends IService<RUserDungeonTable> {

    GetDungeonResponse getApplyDungeon(GetDungeonRequest getDungeonRequest);

    void joinDungeon(JoinDungeonRequest joinDungeonRequest);
}
