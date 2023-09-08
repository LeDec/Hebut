package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Institute;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.AddInstituteRequest;
import com.Hebut.JiangXin.project.entity.request.ShowAllInstituteRequest;
import com.Hebut.JiangXin.project.entity.response.ShowAllInstituteResponse;
import com.Hebut.JiangXin.project.mapper.InstituteMapper;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.Hebut.JiangXin.project.service.IInstituteService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 学院表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@Service
public class InstituteServiceImpl extends ServiceImpl<InstituteMapper, Institute> implements IInstituteService {

    @Resource
    InstituteMapper instituteMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public void addInstitute(AddInstituteRequest addInstituteRequest) {
        Institute institute = new Institute();
        institute.setInstituteId(addInstituteRequest.getInstituteId());
        institute.setInstituteName(addInstituteRequest.getInstituteName());
        instituteMapper.insert(institute);
    }

    @Override
    public PageUtil<ShowAllInstituteResponse> showAllInstitute(ShowAllInstituteRequest showAllInstituteRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showAllInstituteRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showAllInstituteRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证错误");
        }
        List<Institute> institutes = instituteMapper.selectList(
                Wrappers.lambdaQuery(Institute.class)
        );
        List<ShowAllInstituteResponse> showAllInstituteResponses = new ArrayList<>();
        for (Institute institute : institutes) {
            ShowAllInstituteResponse showAllInstituteResponse = new ShowAllInstituteResponse();
            showAllInstituteResponse.setInstituteId(institute.getInstituteId());
            showAllInstituteResponse.setInstituteName(institute.getInstituteName());
            showAllInstituteResponses.add(showAllInstituteResponse);
        }
        PageUtil<ShowAllInstituteResponse> showAllInstituteResponsePageUtil = new PageUtil<>(showAllInstituteRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllInstituteRequest.getCheckRequest().getPageRequest().getPageSize(), showAllInstituteResponses);
        return showAllInstituteResponsePageUtil;
    }

}
