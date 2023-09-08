package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.*;
import com.android.quest.project.entity.request.AddDungeonQuestRequest;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.request.DungeonIntegrationRequest;
import com.android.quest.project.entity.request.GetDungeonQuestRequest;
import com.android.quest.project.entity.response.*;
import com.android.quest.project.mapper.*;
import com.android.quest.project.service.IRDungeonQuestTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class RDungeonQuestTableServiceImpl extends ServiceImpl<RDungeonQuestTableMapper, RDungeonQuestTable> implements IRDungeonQuestTableService {

    @Resource
    RDungeonQuestTableMapper rDungeonQuestTableMapper;
    @Resource
    QuestTableMapper questTableMapper;
    @Resource
    RUserQuestTableMapper rUserQuestTableMapper;
    @Resource
    AdminTableMapper adminTableMapper;
    @Resource
    DungeonTableMapper dungeonTableMapper;
    @Override
    public GetQuestResponse getDungeonQuest(GetDungeonQuestRequest getDungeonQuestRequest) {
        GetQuestResponse getQuestResponse = new GetQuestResponse();
        List<QuestResponse> questResponseList = new ArrayList<>();
        List<RDungeonQuestTable> dungeonQuestTableList = rDungeonQuestTableMapper.selectList(
                Wrappers.lambdaQuery(RDungeonQuestTable.class)
                        .eq(RDungeonQuestTable::getDungeonId,getDungeonQuestRequest.getDungeon_id())
        );
        for(RDungeonQuestTable rDungeonQuestTable : dungeonQuestTableList)
        {
            QuestResponse questResponse = new QuestResponse();
            QuestTable questTable =  questTableMapper.selectOne(
                    Wrappers.lambdaQuery(QuestTable.class)
                            .eq(QuestTable::getId,rDungeonQuestTable.getQuestId())
            );
            RUserQuestTable rUserQuestTable =  rUserQuestTableMapper.selectOne(
                    Wrappers.lambdaQuery(RUserQuestTable.class)
                            .eq(RUserQuestTable::getQuestId,rDungeonQuestTable.getQuestId())
                            .eq(RUserQuestTable::getUserId,getDungeonQuestRequest.getUser_id())
            );
            questResponse.setQuest_id(questTable.getId());
            questResponse.setTitle(questTable.getTitle());
            questResponse.setCoin(questTable.getCoin());
            questResponse.setQuestEnum(questTable.getType());
            questResponse.setSelf_treasure(questTable.getSelfTreasure());
            questResponse.setCombo(rUserQuestTable.getCombo());
            questResponse.setIs_complete(rUserQuestTable.getIsComplete());
            questResponseList.add(questResponse);
        }
        getQuestResponse.setQuestResponseList(questResponseList);
        return getQuestResponse;
    }

    @Override
    public CheckDungeonQuestResponse checkDungeonQuest(CheckRequest checkRequest) {
        CheckDungeonQuestResponse checkDungeonQuestResponse = new CheckDungeonQuestResponse();
        List<DungeonQuestResponse> questList = new ArrayList<>();
        AdminTable admin = null;
        admin = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,checkRequest.getUser_id())
        );
        if(admin == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            List<QuestTable> questTableList = questTableMapper.selectList(
                    Wrappers.lambdaQuery(QuestTable.class)
                            .eq(QuestTable::getSelfTreasure,"副本任务")
            );
            for(QuestTable questTable :questTableList){
                DungeonQuestResponse dungeonQuestResponse = new DungeonQuestResponse();
                dungeonQuestResponse.setQuest_id(questTable.getId());
                dungeonQuestResponse.setTitle(questTable.getTitle());
                dungeonQuestResponse.setQuestEnum(questTable.getType());
                questList.add(dungeonQuestResponse);
            }
            checkDungeonQuestResponse.setQuestList(questList);
            checkDungeonQuestResponse.setCount(questList.size());
        }
        return checkDungeonQuestResponse;
    }

    @Override
    public void addDungeonQuest(AddDungeonQuestRequest addDungeonQuestRequest) {
        AdminTable admin = null;
        admin = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,addDungeonQuestRequest.getAdminId())
        );
        if(admin == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            QuestTable questTable = new QuestTable();
            questTable.setTitle(addDungeonQuestRequest.getTitle());
            questTable.setType(addDungeonQuestRequest.getQuestEnum());
            questTable.setCoin(addDungeonQuestRequest.getCoin());
            questTable.setSelfTreasure("副本任务");
            questTableMapper.insert(questTable);
        }
    }

    @Override
    public void DungeonIntegration(DungeonIntegrationRequest dungeonIntegrationRequest) {
        AdminTable admin = null;
        admin = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,dungeonIntegrationRequest.getAdminId())
        );
        if(admin == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            DungeonTable dungeonTable = new DungeonTable();
            dungeonTable.setTitle(dungeonIntegrationRequest.getTitle());
            dungeonTable.setCoin(dungeonIntegrationRequest.getCoin());
            dungeonTableMapper.insert(dungeonTable);
            for(int questId : dungeonIntegrationRequest.getQuestIdList()){
                RDungeonQuestTable rDungeonQuestTable = new RDungeonQuestTable();
                rDungeonQuestTable.setDungeonId(dungeonTable.getId());
                rDungeonQuestTable.setQuestId(questId);
                rDungeonQuestTableMapper.insert(rDungeonQuestTable);
            }
        }
    }
}
