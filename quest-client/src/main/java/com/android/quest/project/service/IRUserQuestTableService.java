package com.android.quest.project.service;

import com.android.quest.project.entity.RUserQuestTable;
import com.android.quest.project.entity.request.CheckUserQuestRequest;
import com.android.quest.project.entity.request.CollectTotalRequest;
import com.android.quest.project.entity.request.UpdateQuestRequest;
import com.android.quest.project.entity.response.CheckUserQuestResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface IRUserQuestTableService extends IService<RUserQuestTable> {

    void collectTotal(CollectTotalRequest collectTotalRequest);

    void updateQuest(UpdateQuestRequest updateQuestRequest);

    CheckUserQuestResponse checkUserQuest(CheckUserQuestRequest checkUserQuestRequest);
}
