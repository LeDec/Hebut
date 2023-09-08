package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.AchievementEnum;
import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.*;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckQuestInformationResponse;
import com.android.quest.project.entity.response.CompleteQuestResponse;
import com.android.quest.project.entity.response.GetQuestResponse;
import com.android.quest.project.entity.response.QuestResponse;
import com.android.quest.project.mapper.*;
import com.android.quest.project.service.IQuestTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Service
public class QuestTableServiceImpl extends ServiceImpl<QuestTableMapper, QuestTable> implements IQuestTableService {

    @Resource
    QuestTableMapper questTableMapper;
    @Resource
    RUserQuestTableMapper rUserQuestTableMapper;
    @Resource
    RUserDungeonTableMapper rUserDungeonTableMapper;
    @Resource
    RDungeonQuestTableMapper rDungeonQuestTableMapper;
    @Resource
    UserTableMapper userTableMapper;
    @Resource
    QuestInforTableMapper questInforTableMapper;
    @Resource
    AchievementTableMapper achievementTableMapper;
    @Resource
    RUserAchievementTableMapper rUserAchievementTableMapper;

    @Override
    public GetQuestResponse getQuest(GetQuestRequest getQuestRequest) {
        int over = 0;
        if(!Objects.equals(getQuestRequest.getQuest_type(), "daily") &&
                !Objects.equals(getQuestRequest.getQuest_type(), "weekly") &&
                !Objects.equals(getQuestRequest.getQuest_type(), "achievement"))
        {
            throw new CustomException(ErrorEnum.NOT_FOUND_OBJECT.getCode(),"任务类型不正确");
        }
        GetQuestResponse getQuestResponse = new GetQuestResponse();
        List<QuestResponse> questResponseList = new ArrayList<>();
        List<RUserQuestTable> rUserQuestTableList = rUserQuestTableMapper.selectList(
                Wrappers.lambdaQuery(RUserQuestTable.class)
                        .eq(RUserQuestTable::getUserId,getQuestRequest.getUser_id())
        );
        for(RUserQuestTable rUserQuestTable : rUserQuestTableList)
        {
            QuestTable questTable = null;
            questTable = questTableMapper.selectOne(
                    Wrappers.lambdaQuery(QuestTable.class)
                            .eq(QuestTable::getId,rUserQuestTable.getQuestId())
                            .eq(QuestTable::getType,getQuestRequest.getQuest_type())
            );
            if(questTable == null){
                continue;
            }
            QuestResponse questResponse = new QuestResponse();
            questResponse.setQuest_id(questTable.getId());
            questResponse.setTitle(questTable.getTitle());
            questResponse.setCoin(questTable.getCoin());
            questResponse.setQuestEnum(questTable.getType());
            questResponse.setSelf_treasure(questTable.getSelfTreasure());
            questResponse.setCombo(rUserQuestTable.getCombo());
            questResponse.setIs_complete(rUserQuestTable.getIsComplete());
            if(Objects.equals(rUserQuestTable.getIsComplete(), "1"))
            {
                over++;
            }
            questResponseList.add(questResponse);
        }
        getQuestResponse.setCount(questResponseList.size());
        getQuestResponse.setOver(over);
        getQuestResponse.setQuestResponseList(questResponseList);
        return getQuestResponse;
    }

    @Override
    public GetQuestResponse getAppliedDungeonQuest(GetAppliedDungeonRequest getAppliedDungeonRequest) {
        int over = 0;
        List<QuestResponse> questResponseList = new ArrayList<>();
        GetQuestResponse getQuestResponse =  new GetQuestResponse();
        List<RUserDungeonTable> rUserDungeonTableList = rUserDungeonTableMapper.selectList(
                Wrappers.lambdaQuery(RUserDungeonTable.class)
                        .eq(RUserDungeonTable::getUserId,getAppliedDungeonRequest.getUser_id())
        );
        for(RUserDungeonTable rUserDungeonTable : rUserDungeonTableList)
        {
            List<RDungeonQuestTable> rDungeonQuestTableList = rDungeonQuestTableMapper.selectList(
                    Wrappers.lambdaQuery(RDungeonQuestTable.class)
                            .eq(RDungeonQuestTable::getDungeonId,rUserDungeonTable.getDungeonId())
            );
            for(RDungeonQuestTable rDungeonQuestTable : rDungeonQuestTableList)
            {
                QuestTable questTable = questTableMapper.selectOne(
                        Wrappers.lambdaQuery(QuestTable.class)
                                .eq(QuestTable::getId,rDungeonQuestTable.getQuestId())
                );
                RUserQuestTable rUserQuestTable = rUserQuestTableMapper.selectOne(
                        Wrappers.lambdaQuery(RUserQuestTable.class)
                                .eq(RUserQuestTable::getUserId,getAppliedDungeonRequest.getUser_id())
                                .eq(RUserQuestTable::getQuestId,rDungeonQuestTable.getQuestId())
                );
                QuestResponse questResponse = new QuestResponse();
                questResponse.setQuest_id(questTable.getId());
                questResponse.setTitle(questTable.getTitle());
                questResponse.setCoin(questTable.getCoin());
                questResponse.setQuestEnum(questTable.getType());
                questResponse.setSelf_treasure(questTable.getSelfTreasure());
                questResponse.setCombo(rUserQuestTable.getCombo());
                questResponse.setIs_complete(rUserQuestTable.getIsComplete());
                if(Objects.equals(questResponse.getIs_complete(), "1"))
                {
                    over++;
                }
                questResponseList.add(questResponse);
            }
        }
        getQuestResponse.setCount(questResponseList.size());
        getQuestResponse.setOver(over);
        getQuestResponse.setQuestResponseList(questResponseList);
        return getQuestResponse;
    }

    @Override
    public void developQuest(DevelopQuestRequest developQuestRequest) {
        QuestTable questData = questTableMapper.selectOne(
                Wrappers.lambdaQuery(QuestTable.class)
                        .eq(QuestTable::getTitle,developQuestRequest.getTitle())
                        .eq(QuestTable::getType,developQuestRequest.getType())
        );
        if(questData != null){
            RUserQuestTable rUserQuestTable = null;
            rUserQuestTable = rUserQuestTableMapper.selectOne(
                    Wrappers.lambdaQuery(RUserQuestTable.class)
                            .eq(RUserQuestTable::getUserId,developQuestRequest.getUser_id())
                            .eq(RUserQuestTable::getQuestId,questData.getId())

            );
            if(rUserQuestTable != null)
            {
                throw new CustomException(ErrorEnum.EXISTED_OBJECT.getCode(),"您已制定相同任务！");
            }
        }
        QuestTable questTable = new QuestTable();
        questTable.setTitle(developQuestRequest.getTitle());
        questTable.setType(developQuestRequest.getType());
        questTable.setSelfTreasure(developQuestRequest.getSelf_treasure());
        questTable.setCoin(developQuestRequest.getCoin());
        questTableMapper.insert(questTable);
        RUserQuestTable rUserQuestTable = new RUserQuestTable();
        rUserQuestTable.setUserId(developQuestRequest.getUser_id());
        rUserQuestTable.setQuestId(questTable.getId());
        rUserQuestTable.setUpdateTime(LocalDate.now());
        rUserQuestTableMapper.insert(rUserQuestTable);
    }

    @Override
    public CompleteQuestResponse completeQuest(CompleteQuestRequest completeQuestRequest) {
        RUserQuestTable userQuestTable = null;
        CompleteQuestResponse completeQuestResponse = new CompleteQuestResponse();
        QuestTable questTable = questTableMapper.selectOne(
                Wrappers.lambdaQuery(QuestTable.class)
                        .eq(QuestTable::getId,completeQuestRequest.getQuest_id())
        );
        completeQuestResponse.set_achievement(false);
        userQuestTable = rUserQuestTableMapper.selectOne(
                Wrappers.lambdaQuery(RUserQuestTable.class)
                        .eq(RUserQuestTable::getUserId,completeQuestRequest.getUser_id())
                        .eq(RUserQuestTable::getQuestId,completeQuestRequest.getQuest_id())
        );
        if(userQuestTable == null){
            throw new CustomException(ErrorEnum.NOT_FOUND_OBJECT.getCode(),"用户未制定该任务！");
        }else if(userQuestTable.getIsComplete() == "1"){
            throw new CustomException(ErrorEnum.COMPLETED.getCode(),"用户已完成该任务");
        }
        userQuestTable.setIsComplete("1");
        userQuestTable.setCombo(userQuestTable.getCombo() + 1);
        userQuestTable.setUpdateTime(LocalDate.now());
        UserTable userTable = userTableMapper.selectOne(
                Wrappers.lambdaQuery(UserTable.class)
                        .eq(UserTable::getId,completeQuestRequest.getUser_id())
        );
        userTable.setCoin(userTable.getCoin() + questTable.getCoin());
        String achievementId = UUID.randomUUID().toString();
        if(Objects.equals(questTable.getType(), "daily") && userQuestTable.getCombo() == 7 || userQuestTable.getCombo() == 30){
            AchievementTable achievementTable = new AchievementTable();
            achievementTable.setId(achievementId);
            achievementTable.setQuestId(questTable.getId());
            achievementTable.setTitle(questTable.getTitle() + " 坚持 " + userQuestTable.getCombo() + "次");
            achievementTable.setType(AchievementEnum.QUEST.getMsg());
            achievementTable.setSoundRecording("null");
            achievementTableMapper.insert(achievementTable);
            completeQuestResponse.set_achievement(true);
            completeQuestResponse.setAchievement_id(achievementId);
            RUserAchievementTable userAchievementTable = new RUserAchievementTable();
            userAchievementTable.setAchievementId(achievementId);
            userAchievementTable.setUserId(completeQuestRequest.getUser_id());
            rUserAchievementTableMapper.insert(userAchievementTable);
            rUserQuestTableMapper.updateById(userQuestTable);
        }
        if(Objects.equals(questTable.getType(), "achievement")){
            AchievementTable achievementTable = new AchievementTable();
            achievementTable.setId(achievementId);
            achievementTable.setQuestId(questTable.getId());
            achievementTable.setTitle(questTable.getTitle());
            achievementTable.setType(AchievementEnum.QUEST.getMsg());
            achievementTable.setSoundRecording("null");
            achievementTableMapper.insert(achievementTable);
            completeQuestResponse.set_achievement(true);
            completeQuestResponse.setAchievement_id(achievementId);
            RUserAchievementTable userAchievementTable = new RUserAchievementTable();
            userAchievementTable.setAchievementId(achievementId);
            userAchievementTable.setUserId(completeQuestRequest.getUser_id());
            rUserAchievementTableMapper.insert(userAchievementTable);
            rUserQuestTableMapper.deleteById(userQuestTable);
        }
        QuestInforTable questInforTable = new QuestInforTable();
        questInforTable.setQuestId(userQuestTable.getQuestId());
        questInforTable.setUserId(userQuestTable.getUserId());
        questInforTable.setCreateTime(LocalDate.now());
        questInforTableMapper.insert(questInforTable);
        return completeQuestResponse;
    }

    @Override
    public CheckQuestInformationResponse checkQuestInformation(CheckQuestInformationRequest checkQuestInformationRequest) {
        RUserQuestTable rUserQuestTable = null;
        rUserQuestTable = rUserQuestTableMapper.selectOne(
                Wrappers.lambdaQuery(RUserQuestTable.class)
                        .eq(RUserQuestTable::getUserId,checkQuestInformationRequest.getUser_id())
                        .eq(RUserQuestTable::getQuestId,checkQuestInformationRequest.getQuest_id())
        );
        if(rUserQuestTable == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"只能查看自己制定的任务！");
        }
        CheckQuestInformationResponse checkQuestInformationResponse = new CheckQuestInformationResponse();
        QuestTable questTable = questTableMapper.selectOne(
                Wrappers.lambdaQuery(QuestTable.class)
                        .eq(QuestTable::getId,rUserQuestTable.getQuestId())
        );
        checkQuestInformationResponse.setQuest_id(questTable.getId());
        checkQuestInformationResponse.setTitle(questTable.getTitle());
        checkQuestInformationResponse.setCoin(questTable.getCoin());
        checkQuestInformationResponse.setQuestEnum(questTable.getType());
        checkQuestInformationResponse.setSelf_treasure(questTable.getSelfTreasure());
        checkQuestInformationResponse.setCombo(rUserQuestTable.getCombo());
        checkQuestInformationResponse.setIs_complete(rUserQuestTable.getIsComplete());
        return checkQuestInformationResponse;
    }
}
