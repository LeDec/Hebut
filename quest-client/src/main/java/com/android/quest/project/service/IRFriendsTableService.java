package com.android.quest.project.service;

import com.android.quest.project.entity.RFriendsTable;
import com.android.quest.project.entity.request.GetFriendsRequest;
import com.android.quest.project.entity.response.GetFriendsResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface IRFriendsTableService extends IService<RFriendsTable> {

    GetFriendsResponse getFriends(GetFriendsRequest getFriendsRequest);
}
