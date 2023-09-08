package com.android.quest.project.service.impl;

import com.android.quest.project.entity.QuestInforTable;
import com.android.quest.project.entity.QuestTable;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.CheckQuestInforResponse;
import com.android.quest.project.mapper.QuestInforTableMapper;
import com.android.quest.project.mapper.QuestTableMapper;
import com.android.quest.project.service.IQuestInforTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Service
public class QuestInforTableServiceImpl extends ServiceImpl<QuestInforTableMapper, QuestInforTable> implements IQuestInforTableService {

    @Resource
    QuestInforTableMapper questInforTableMapper;
    @Resource
    QuestTableMapper questTableMapper;
    @Override
    public List<CheckQuestInforResponse> checkQuestInfor(CheckRequest checkRequest) {
        List<CheckQuestInforResponse> checkQuestInforResponseList = new ArrayList<>();
        List<QuestInforTable> questInforTableList = questInforTableMapper.selectList(
                Wrappers.lambdaQuery(QuestInforTable.class)
                        .eq(QuestInforTable::getUserId,checkRequest.getUser_id())
        );
        for(QuestInforTable questInforTable : questInforTableList){
            CheckQuestInforResponse checkQuestInforResponse = new CheckQuestInforResponse();
            checkQuestInforResponse.set_id(questInforTable.getId());
            checkQuestInforResponse.setQuestId(questInforTable.getQuestId());
            QuestTable questTable = questTableMapper.selectOne(
                    Wrappers.lambdaQuery(QuestTable.class)
                            .eq(QuestTable::getId,questInforTable.getQuestId())
            );
            checkQuestInforResponse.setTitle(questTable.getTitle());
            if(Objects.equals(questTable.getType(), "daily")){
                checkQuestInforResponse.setType("每日任务");
            }else if (Objects.equals(questTable.getType(), "weekly")){
                checkQuestInforResponse.setType("每周任务");
            } else{
                checkQuestInforResponse.setType("成就任务");
            }
            checkQuestInforResponse.setCreateTime(questInforTable.getCreateTime().toString());
            checkQuestInforResponseList.add(checkQuestInforResponse);
        }
        return checkQuestInforResponseList;
    }
}
