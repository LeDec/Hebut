package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.*;
import com.android.quest.project.entity.request.GetDungeonRequest;
import com.android.quest.project.entity.request.JoinDungeonRequest;
import com.android.quest.project.entity.response.*;
import com.android.quest.project.mapper.*;
import com.android.quest.project.service.IRUserDungeonTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Service
public class RUserDungeonTableServiceImpl extends ServiceImpl<RUserDungeonTableMapper, RUserDungeonTable> implements IRUserDungeonTableService {

    @Resource
    RUserDungeonTableMapper rUserDungeonTableMapper;
    @Resource
    DungeonTableMapper dungeonTableMapper;

    @Resource
    RDungeonQuestTableMapper rDungeonQuestTableMapper;

    @Resource
    QuestTableMapper questTableMapper;

    @Resource
    RUserQuestTableMapper rUserQuestTableMapper;

    @Override
    public GetDungeonResponse getApplyDungeon(GetDungeonRequest getDungeonRequest) {
        GetDungeonResponse getDungeonResponse = new GetDungeonResponse();
        List<DungeonResponse> dungeonResponseList = new ArrayList<>();
        List<DungeonTable> dungeonTableList = dungeonTableMapper.selectList(
                Wrappers.lambdaQuery(DungeonTable.class)
        );
        for(DungeonTable dungeonTable : dungeonTableList)
        {
            DungeonResponse dungeonResponse = new DungeonResponse();
            List<DungeonQuestResponse> dungeonQuestResponseList = new ArrayList<>();
            dungeonResponse.setDungeon_id(dungeonTable.getId());
            dungeonResponse.setTitle(dungeonTable.getTitle());
            dungeonResponse.setCoin(dungeonTable.getCoin());
            RUserDungeonTable rUserDungeonTable = null;
            rUserDungeonTable = rUserDungeonTableMapper.selectOne(
                    Wrappers.lambdaQuery(RUserDungeonTable.class)
                            .eq(RUserDungeonTable::getUserId,getDungeonRequest.getUser_id())
                            .eq(RUserDungeonTable::getDungeonId,dungeonTable.getId())
            );
            dungeonResponse.setIs_complete(rUserDungeonTable == null ? 0 : 1);
            List<RDungeonQuestTable> rDungeonQuestTableList = rDungeonQuestTableMapper.selectList(
                    Wrappers.lambdaQuery(RDungeonQuestTable.class)
                            .eq(RDungeonQuestTable::getDungeonId,dungeonTable.getId())
            );
            for(RDungeonQuestTable rDungeonQuestTable : rDungeonQuestTableList)
            {
                DungeonQuestResponse dungeonQuestResponse = new DungeonQuestResponse();
                QuestTable questTable = questTableMapper.selectOne(
                        Wrappers.lambdaQuery(QuestTable.class)
                                .eq(QuestTable::getId,rDungeonQuestTable.getQuestId())
                );
                dungeonQuestResponse.setQuest_id(questTable.getId());
                dungeonQuestResponse.setQuestEnum(questTable.getType());
                dungeonQuestResponse.setTitle(questTable.getTitle());
                dungeonQuestResponseList.add(dungeonQuestResponse);
            }
            dungeonResponse.setQuestList(dungeonQuestResponseList);
            dungeonResponseList.add(dungeonResponse);
        }
        getDungeonResponse.setDungeonResponseList(dungeonResponseList);
        return getDungeonResponse;
    }

    @Override
    public void joinDungeon(JoinDungeonRequest joinDungeonRequest) {
        RUserDungeonTable rUserDungeonTable = null;
        rUserDungeonTable = rUserDungeonTableMapper.selectOne(
                Wrappers.lambdaQuery(RUserDungeonTable.class)
                        .eq(RUserDungeonTable::getUserId,joinDungeonRequest.getUser_id())
                        .eq(RUserDungeonTable::getDungeonId,joinDungeonRequest.getDungeon_id())
        );
        if (rUserDungeonTable == null){
            rUserDungeonTable = new RUserDungeonTable();
            rUserDungeonTable.setUserId(joinDungeonRequest.getUser_id());
            rUserDungeonTable.setDungeonId(joinDungeonRequest.getDungeon_id());
            List<RDungeonQuestTable> rDungeonQuestTableList = rDungeonQuestTableMapper.selectList(
                    Wrappers.lambdaQuery(RDungeonQuestTable.class)
                            .eq(RDungeonQuestTable::getDungeonId,joinDungeonRequest.getDungeon_id())
            );
            for(RDungeonQuestTable rDungeonQuestTable : rDungeonQuestTableList){
                RUserQuestTable rUserQuestTable = new RUserQuestTable();
                rUserQuestTable.setQuestId(rDungeonQuestTable.getQuestId());
                rUserQuestTable.setUserId(joinDungeonRequest.getUser_id());
                rUserQuestTable.setUpdateTime(LocalDate.now());
                rUserQuestTableMapper.insert(rUserQuestTable);
            }
            rUserDungeonTableMapper.insert(rUserDungeonTable);
        }else {
            throw new CustomException(ErrorEnum.COMPLETED.getCode(),"已经参与副本！");
        }
    }
}
