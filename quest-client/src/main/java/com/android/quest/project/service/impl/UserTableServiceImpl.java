package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.AdminTable;
import com.android.quest.project.entity.RUserAchievementTable;
import com.android.quest.project.entity.UserTable;
import com.android.quest.project.entity.request.*;
import com.android.quest.project.entity.response.CheckUserListResponse;
import com.android.quest.project.entity.response.CheckUserResponse;
import com.android.quest.project.entity.response.LoginResponse;
import com.android.quest.project.entity.response.MyInformationResponse;
import com.android.quest.project.mapper.AdminTableMapper;
import com.android.quest.project.mapper.RUserAchievementTableMapper;
import com.android.quest.project.mapper.UserTableMapper;
import com.android.quest.project.service.IUserTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Service
public class UserTableServiceImpl extends ServiceImpl<UserTableMapper, UserTable> implements IUserTableService {

    @Resource
    UserTableMapper userTableMapper;
    @Resource
    RUserAchievementTableMapper rUserAchievementTableMapper;
    @Resource
    AdminTableMapper adminTableMapper;
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserTable userTable = null;
        userTable = userTableMapper.selectOne(
                Wrappers.lambdaQuery(UserTable.class)
                        .eq(UserTable::getPhone,loginRequest.getPhone())
                        .eq(UserTable::getPassword,loginRequest.getPassword())
        );
        if(userTable == null)
        {
            throw new CustomException(ErrorEnum.LOGIN_ERROR.getCode(),"账号或密码错误！");
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(userTable.getId());
        return loginResponse;
    }

    @Override
    public MyInformationResponse myInformation(MyInformationRequest myInformationRequest) {
        MyInformationResponse myInformationResponse = new MyInformationResponse();
        UserTable userTable = userTableMapper.selectOne(
                Wrappers.lambdaQuery(UserTable.class)
                        .eq(UserTable::getId,myInformationRequest.getUser_id())
        );
        myInformationResponse.setUser_id(userTable.getId());
        myInformationResponse.setNickname(userTable.getNickname());
        myInformationResponse.setProfile(userTable.getProfile());
        myInformationResponse.setCoin(userTable.getCoin());
        myInformationResponse.setAchievement_count(
                rUserAchievementTableMapper.selectCount(
                        Wrappers.lambdaQuery(RUserAchievementTable.class)
                                .eq(RUserAchievementTable::getUserId,userTable.getId())
                )
        );
        return myInformationResponse;
    }

    @Override
    public LoginResponse register(RegisterRequest registerRequest) {
        UserTable user = null;
        user = userTableMapper.selectOne(
                Wrappers.lambdaQuery(UserTable.class)
                        .eq(UserTable::getPhone,registerRequest.getPhone())
        );
        if(user != null){
            throw new CustomException(ErrorEnum.EXISTED_OBJECT.getCode(),"手机号已经注册");
        }
        LoginResponse loginResponse = new LoginResponse();
        UserTable userTable = new UserTable();
        userTable.setPhone(registerRequest.getPhone());
        userTable.setPassword(registerRequest.getPassword());
        String nameCode = UUID.randomUUID().toString().substring(0, 6);
        userTable.setNickname("冒险家" + nameCode);
        userTable.setProfile("1");
        userTable.setUpdateTime(LocalDate.now());
        userTableMapper.insert(userTable);
        UserTable newUser = userTableMapper.selectOne(
                    Wrappers.lambdaQuery(UserTable.class)
                            .eq(UserTable::getPhone,registerRequest.getPhone())
        );
        loginResponse.setUserId(newUser.getId());
        return loginResponse;
    }

    @Override
    public LoginResponse forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        UserTable userTable = null;
        userTable = userTableMapper.selectOne(
                Wrappers.lambdaQuery(UserTable.class)
                        .eq(UserTable::getPhone,forgetPasswordRequest.getPhone())
        );
        if(userTable == null){
            throw new CustomException(ErrorEnum.NOT_FOUND_OBJECT.getCode(),"您还没有注册");
        }else{
            userTable.setPassword(forgetPasswordRequest.getNew_password());
            userTableMapper.updateById(userTable);
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(userTable.getId());
        return loginResponse;
    }

    @Override
    public void changeInformation(ChangeInformationRequest changeInformationRequest) {
        userTableMapper.update(
                null,
                Wrappers.lambdaUpdate(UserTable.class)
                        .eq(UserTable::getId,changeInformationRequest.getUser_id())
                        .set(UserTable::getNickname,changeInformationRequest.getNickname())
                        .set(UserTable::getProfile,changeInformationRequest.getProfile())
        );
    }

    @Override
    public void changeProfile(ChangeProfileRequest changeProfileRequest) {
        userTableMapper.update(
                null,
                Wrappers.lambdaUpdate(UserTable.class)
                        .eq(UserTable::getId,changeProfileRequest.getUser_id())
                        .set(UserTable::getProfile,changeProfileRequest.getProfile())
        );
    }

    @Override
    public CheckUserListResponse checkUserList(CheckUserListRequest checkUserListRequest) {
        CheckUserListResponse checkUserListResponse = new CheckUserListResponse();
        List<CheckUserResponse> checkUserResponseList = new ArrayList<>();
        AdminTable adminTable = null;
        adminTable = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getId,checkUserListRequest.getAdminId())
        );
        if(adminTable == null){
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(),"权限不正确！");
        }else {
            List<UserTable> userTableList = userTableMapper.selectList(
                    Wrappers.lambdaQuery(UserTable.class)
            );
            for(UserTable userTable : userTableList){
                CheckUserResponse checkUserResponse = new CheckUserResponse();
                checkUserResponse.setUserId(userTable.getId());
                checkUserResponse.setPhone(userTable.getPhone());
                checkUserResponse.setNickname(userTable.getNickname());
                checkUserResponse.setCoin(userTable.getCoin());
                checkUserResponseList.add(checkUserResponse);
            }
            checkUserListResponse.setCheckUserResponseList(checkUserResponseList);
            checkUserListResponse.setCount(checkUserResponseList.size());
            return checkUserListResponse;
        }
    }
}
