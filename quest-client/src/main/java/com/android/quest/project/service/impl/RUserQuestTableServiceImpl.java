package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.Enum.QuestEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.*;
import com.android.quest.project.entity.request.CheckUserQuestRequest;
import com.android.quest.project.entity.request.CollectTotalRequest;
import com.android.quest.project.entity.request.UpdateQuestRequest;
import com.android.quest.project.entity.response.CheckUserQuestResponse;
import com.android.quest.project.entity.response.QuestResponse;
import com.android.quest.project.mapper.*;
import com.android.quest.project.service.IRUserQuestTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Service
public class RUserQuestTableServiceImpl extends ServiceImpl<RUserQuestTableMapper, RUserQuestTable> implements IRUserQuestTableService {

    @Resource
    RUserQuestTableMapper rUserQuestTableMapper;
    @Resource
    UserTableMapper userTableMapper;
    @Resource
    QuestTableMapper questTableMapper;
    @Resource
    AdminTableMapper adminTableMapper;

    @Override
    public void collectTotal(CollectTotalRequest collectTotalRequest) {
        int quest = 0;
        List<RUserQuestTable> rUserQuestTableList = rUserQuestTableMapper.selectList(
                Wrappers.lambdaQuery(RUserQuestTable.class)
                        .eq(RUserQuestTable::getUserId, collectTotalRequest.getUser_id())
        );
        for (RUserQuestTable rUserQuestTable : rUserQuestTableList) {
            QuestTable questTable = null;
            questTable = questTableMapper.selectOne(
                    Wrappers.lambdaQuery(QuestTable.class)
                            .eq(QuestTable::getId, rUserQuestTable.getQuestId())
                            .eq(QuestTable::getType, collectTotalRequest.getQuest_type())
            );
            if (questTable != null) {
                quest++;
            }
        }
        UserTable userTable = userTableMapper.selectOne(
                Wrappers.lambdaQuery(UserTable.class)
                        .eq(UserTable::getId, collectTotalRequest.getUser_id())
        );
        if (Objects.equals(collectTotalRequest.getQuest_type(), QuestEnum.DAILY.getMsg())) {
            userTable.setIsCompleteDaily("1");
            userTable.setCoin(userTable.getCoin() + 10 * quest);
            if(Objects.equals(userTable.getIsCompleteDaily(), "1")){
                throw new CustomException(ErrorEnum.COMPLETED.getCode(),"已经领取");
            }
            userTable.setIsCompleteDaily("1");
        } else if (Objects.equals(collectTotalRequest.getQuest_type(), QuestEnum.WEEKLY.getMsg())) {
            userTable.setIsCompleteWeekly("1");
            userTable.setCoin(userTable.getCoin() + 50 * quest);
            if(Objects.equals(userTable.getIsCompleteWeekly(), "1")){
                throw new CustomException(ErrorEnum.COMPLETED.getCode(),"已经领取");
            }
            userTable.setIsCompleteWeekly("1");
        } else {
            throw new CustomException(ErrorEnum.NOT_FOUND_OBJECT.getCode(), "任务类型不正确");
        }
        userTableMapper.updateById(userTable);
    }

    @Override
    public void updateQuest(UpdateQuestRequest updateQuestRequest) {
        List<RUserQuestTable> rUserQuestTableList = rUserQuestTableMapper.selectList(
                Wrappers.lambdaQuery(RUserQuestTable.class)
                        .eq(RUserQuestTable::getUserId,updateQuestRequest.getUser_id())
        );
        LocalDate localDate = LocalDate.now();
        for(RUserQuestTable rUserQuestTable : rUserQuestTableList){
            QuestTable questTable = questTableMapper.selectOne(
                    Wrappers.lambdaQuery(QuestTable.class)
                            .eq(QuestTable::getId,rUserQuestTable.getQuestId())
            );
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY,1);
            if(Objects.equals(questTable.getType(), "daily") && rUserQuestTable.getUpdateTime().isBefore(localDate)){
               if(Objects.equals(rUserQuestTable.getIsComplete(), "0"))
               {
                   rUserQuestTable.setCombo(0);
               }
                rUserQuestTable.setIsComplete("0");
            }else if(Objects.equals(questTable.getType(),"weekly") && rUserQuestTable.getUpdateTime().get(weekFields.weekOfWeekBasedYear()) != localDate.get(weekFields.weekOfWeekBasedYear())){
                if(Objects.equals(rUserQuestTable.getIsComplete(), "0"))
                {
                    rUserQuestTable.setCombo(0);
                }
                rUserQuestTable.setIsComplete("0");
            }
            rUserQuestTableMapper.updateById(rUserQuestTable);
        }
    }

    @Override
    public CheckUserQuestResponse checkUserQuest(CheckUserQuestRequest checkUserQuestRequest) {
        int over = 0;
        CheckUserQuestResponse checkUserQuestResponse = new CheckUserQuestResponse();
        List<QuestResponse> questResponseList = new ArrayList<>();
        AdminTable adminTable = null;
        adminTable = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,checkUserQuestRequest.getAdminId())
        );
        if(adminTable == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            UserTable user = userTableMapper.selectOne(
                    Wrappers.lambdaQuery(UserTable.class)
                            .eq(UserTable::getId,checkUserQuestRequest.getUserId())
            );
            List<RUserQuestTable> rUserQuestTableList = rUserQuestTableMapper.selectList(
                    Wrappers.lambdaQuery(RUserQuestTable.class)
                            .eq(RUserQuestTable::getUserId,checkUserQuestRequest.getUserId())
            );
            for(RUserQuestTable rUserQuestTable : rUserQuestTableList){
                QuestTable questTable = questTableMapper.selectOne(
                        Wrappers.lambdaQuery(QuestTable.class)
                                .eq(QuestTable::getId,rUserQuestTable.getQuestId())
                );
                QuestResponse questResponse = new QuestResponse();
                questResponse.setQuest_id(questTable.getId());
                questResponse.setTitle(questTable.getTitle());
                questResponse.setQuestEnum(questTable.getType());
                questResponse.setCoin(questTable.getCoin());
                questResponse.setCombo(rUserQuestTable.getCombo());
                questResponse.setSelf_treasure(questTable.getSelfTreasure());
                questResponse.setIs_complete(rUserQuestTable.getIsComplete());
                if(Objects.equals(rUserQuestTable.getIsComplete(), "1")){
                    over++;
                }
                questResponseList.add(questResponse);
            }
            questResponseList.sort(QuestResponse::compareTo);
            checkUserQuestResponse.setQuestResponseList(questResponseList);
            checkUserQuestResponse.setCount(questResponseList.size());
            checkUserQuestResponse.setOver(over);
            checkUserQuestResponse.setIsDailyOver(user.getIsCompleteDaily());
            checkUserQuestResponse.setIsWeeklyOver(user.getIsCompleteWeekly());
        }
        return checkUserQuestResponse;
    }
}
