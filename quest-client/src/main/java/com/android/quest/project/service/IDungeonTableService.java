package com.android.quest.project.service;

import com.android.quest.project.entity.DungeonTable;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.request.DeleteDungeonRequest;
import com.android.quest.project.entity.response.CheckDungeonListResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface IDungeonTableService extends IService<DungeonTable> {

    CheckDungeonListResponse checkDungeonList(CheckRequest checkRequest);

    void deleteDungeon(DeleteDungeonRequest deleteDungeonRequest);
}
