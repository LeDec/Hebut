package com.android.quest.project.service;

import com.android.quest.project.entity.RDungeonQuestTable;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckDungeonQuestResponse;
import com.android.quest.project.entity.response.CheckUserQuestResponse;
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
public interface IRDungeonQuestTableService extends IService<RDungeonQuestTable> {

    GetQuestResponse getDungeonQuest(GetDungeonQuestRequest getDungeonQuestRequest);

    CheckDungeonQuestResponse checkDungeonQuest(CheckRequest checkRequest);

    void addDungeonQuest(AddDungeonQuestRequest addDungeonQuestRequest);

    void DungeonIntegration(DungeonIntegrationRequest dungeonIntegrationRequest);
}
