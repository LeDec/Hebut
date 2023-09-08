package com.android.quest.project.service;

import com.android.quest.project.entity.QuestInforTable;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.CheckQuestInforResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface IQuestInforTableService extends IService<QuestInforTable> {

    List<CheckQuestInforResponse> checkQuestInfor(CheckRequest checkRequest);
}
