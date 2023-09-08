package com.android.quest.project.service;

import com.android.quest.project.entity.UserTable;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckUserListResponse;
import com.android.quest.project.entity.response.LoginResponse;
import com.android.quest.project.entity.response.MyInformationResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
public interface IUserTableService extends IService<UserTable> {

    LoginResponse login(LoginRequest loginRequest);

    MyInformationResponse myInformation(MyInformationRequest myInformationRequest);

    LoginResponse register(RegisterRequest registerRequest);

    LoginResponse forgetPassword(ForgetPasswordRequest forgetPasswordRequest);

    void changeInformation(ChangeInformationRequest changeInformationRequest);

    void changeProfile(ChangeProfileRequest changeProfileRequest);

    CheckUserListResponse checkUserList(CheckUserListRequest checkUserListRequest);
}
