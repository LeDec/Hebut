package com.android.quest.project.service.impl;

import com.android.quest.common.Enum.ErrorEnum;
import com.android.quest.common.exception.CustomException;
import com.android.quest.project.entity.AdminTable;
import com.android.quest.project.entity.request.AdminLoginRequest;
import com.android.quest.project.entity.request.LoginRequest;
import com.android.quest.project.entity.response.LoginResponse;
import com.android.quest.project.mapper.AdminTableMapper;
import com.android.quest.project.service.IAdminTableService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liDong
 * @since 2023-04-28
 */
@Service
public class AdminTableServiceImpl extends ServiceImpl<AdminTableMapper, AdminTable> implements IAdminTableService {

    @Resource
    AdminTableMapper adminTableMapper;

    @Override
    public LoginResponse adminLogin(AdminLoginRequest adminLoginRequest) {
        AdminTable admin = null;
        admin = adminTableMapper.selectOne(
                Wrappers.lambdaQuery(AdminTable.class)
                        .eq(AdminTable::getUserName,adminLoginRequest.getUserName())
                        .eq(AdminTable::getPassword,adminLoginRequest.getPassword())
        );
        if (admin == null){
            throw new CustomException(ErrorEnum.LOGIN_ERROR.getCode(),"账号或密码错误");
        }else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(admin.getId());
            return loginResponse;
        }
    }
}
