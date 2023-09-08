package com.android.quest.project.service;

import com.android.quest.project.entity.QuestTable;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckQuestInformationResponse;
import com.android.quest.project.entity.response.CompleteQuestResponse;
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
public interface IQuestTableService extends IService<QuestTable> {

    GetQuestResponse getQuest(GetQuestRequest getQuestRequest);

    GetQuestResponse getAppliedDungeonQuest(GetAppliedDungeonRequest getAppliedDungeonRequest);

    void developQuest(DevelopQuestRequest developQuestRequest);

    CompleteQuestResponse completeQuest(CompleteQuestRequest completeQuestRequest);

    CheckQuestInformationResponse checkQuestInformation(CheckQuestInformationRequest checkQuestInformationRequest);
}
