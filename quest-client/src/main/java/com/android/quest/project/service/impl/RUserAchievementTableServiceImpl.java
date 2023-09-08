package com.android.quest.project.service.impl;

import com.android.quest.project.entity.AchievementTable;
import com.android.quest.project.entity.DungeonTable;
import com.android.quest.project.entity.QuestTable;
import com.android.quest.project.entity.RUserAchievementTable;
import com.android.quest.project.entity.request.CheckRequest;
import com.android.quest.project.entity.response.AchievementResponse;
import com.android.quest.project.entity.response.GetAchievementResponse;
import com.android.quest.project.mapper.AchievementTableMapper;
import com.android.quest.project.mapper.DungeonTableMapper;
import com.android.quest.project.mapper.QuestTableMapper;
import com.android.quest.project.mapper.RUserAchievementTableMapper;
import com.android.quest.project.service.IRUserAchievementTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
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
public class RUserAchievementTableServiceImpl extends ServiceImpl<RUserAchievementTableMapper, RUserAchievementTable> implements IRUserAchievementTableService {

    @Resource
    RUserAchievementTableMapper rUserAchievementTableMapper;
    @Resource
    QuestTableMapper questTableMapper;
    @Resource
    DungeonTableMapper dungeonTableMapper;
    @Mapper
    AchievementTableMapper achievementTableMapper;

    @Override
    public GetAchievementResponse getAchievementList(CheckRequest checkRequest) {
        GetAchievementResponse getAchievementResponse = new GetAchievementResponse();
        List<AchievementResponse> achievementResponseList = new ArrayList<>();
        List<RUserAchievementTable> rUserAchievementTableList = rUserAchievementTableMapper.selectList(
                Wrappers.lambdaQuery(RUserAchievementTable.class)
                        .eq(RUserAchievementTable::getUserId,checkRequest.getUser_id())
        );
        for(RUserAchievementTable rUserAchievementTable : rUserAchievementTableList)
        {
            AchievementResponse achievementResponse = new AchievementResponse();
            AchievementTable achievementTable = achievementTableMapper.selectOne(
                    Wrappers.lambdaQuery(AchievementTable.class)
                            .eq(AchievementTable::getId,rUserAchievementTable.getAchievementId())
            );
            achievementResponse.setAchievement_id(achievementTable.getId());
            achievementResponse.setTitle(achievementTable.getTitle());
            achievementResponse.setType(achievementTable.getType());
            achievementResponseList.add(achievementResponse);
        }
        getAchievementResponse.setAchievementResponseList(achievementResponseList);
        getAchievementResponse.setCount(achievementResponseList.size());
        return getAchievementResponse;
    }
}
