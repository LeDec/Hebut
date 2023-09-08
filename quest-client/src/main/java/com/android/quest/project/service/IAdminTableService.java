package com.android.quest.project.service;

import com.android.quest.project.entity.AdminTable;
import com.android.quest.project.entity.request.AdminLoginRequest;
import com.android.quest.project.entity.request.LoginRequest;
import com.android.quest.project.entity.response.LoginResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liDong
 * @since 2023-04-28
 */
public interface IAdminTableService extends IService<AdminTable> {

    LoginResponse adminLogin(AdminLoginRequest adminLoginRequest);
}
