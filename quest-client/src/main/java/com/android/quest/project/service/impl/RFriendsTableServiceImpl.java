package com.android.quest.project.service.impl;

import com.android.quest.project.entity.RFriendsTable;
import com.android.quest.project.entity.RUserAchievementTable;
import com.android.quest.project.entity.UserTable;
import com.android.quest.project.entity.request.GetFriendsRequest;
import com.android.quest.project.entity.response.FriendsResponse;
import com.android.quest.project.entity.response.GetFriendsResponse;
import com.android.quest.project.mapper.RFriendsTableMapper;
import com.android.quest.project.mapper.RUserAchievementTableMapper;
import com.android.quest.project.mapper.UserTableMapper;
import com.android.quest.project.service.IRFriendsTableService;
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
public class RFriendsTableServiceImpl extends ServiceImpl<RFriendsTableMapper, RFriendsTable> implements IRFriendsTableService {


    @Resource
    RFriendsTableMapper friendsTableMapper;
    @Resource
    UserTableMapper userTableMapper;
    @Resource
    RUserAchievementTableMapper rUserAchievementTableMapper;
    @Override
    public GetFriendsResponse getFriends(GetFriendsRequest getFriendsRequest) {
        List<RFriendsTable> rFriendsTableList = friendsTableMapper.selectList(
                Wrappers.lambdaQuery(RFriendsTable.class)
                        .eq(RFriendsTable::getUserId,getFriendsRequest.getUser_id())
        );
        GetFriendsResponse getFriendsResponse = new GetFriendsResponse();
        List<FriendsResponse> friendsResponseList = new ArrayList<>();
        for(RFriendsTable rFriendsTable : rFriendsTableList){
            UserTable userTable = userTableMapper.selectOne(
                    Wrappers.lambdaQuery(UserTable.class)
                            .eq(UserTable::getId,rFriendsTable.getFriendId())
            );
            FriendsResponse friendsResponse = new FriendsResponse();
            friendsResponse.setUser_id(userTable.getId());
            friendsResponse.setNickname(userTable.getNickname());
            friendsResponse.setProfile(userTable.getProfile());
            friendsResponse.setCoin(userTable.getCoin());
            friendsResponse.setAchievement(
                    rUserAchievementTableMapper.selectCount(
                            Wrappers.lambdaQuery(RUserAchievementTable.class)
                                    .eq(RUserAchievementTable::getUserId,userTable.getId())
                    )
            );
            friendsResponseList.add(friendsResponse);
        }
        getFriendsResponse.setFriendsResponseList(friendsResponseList);
        return getFriendsResponse;
    }
}
