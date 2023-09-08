package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Major;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.AddMajorRequest;
import com.Hebut.JiangXin.project.entity.request.ShowAllMajorRequest;
import com.Hebut.JiangXin.project.entity.response.ShowAllMajorResponse;
import com.Hebut.JiangXin.project.mapper.MajorMapper;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.Hebut.JiangXin.project.service.IMajorService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 专业表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements IMajorService {
    @Resource
    MajorMapper majorMapper;
    @Resource
    UserMapper userMapper;

    @Override
    public void addMajor(AddMajorRequest addMajorRequest) {
        Major major = new Major();
        major.setMajorId(addMajorRequest.getMajorId());
        major.setMajorName(addMajorRequest.getMajorName());
        majorMapper.insert(major);
    }

    @Override
    public PageUtil<ShowAllMajorResponse> showAllMajor(ShowAllMajorRequest showAllMajorRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, showAllMajorRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, showAllMajorRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证错误");
        }
        List<Major> majors = majorMapper.selectList(
                Wrappers.lambdaQuery(Major.class)
        );
        List<ShowAllMajorResponse> showAllMajorResponses = new ArrayList<>();
        for (Major major : majors) {
            ShowAllMajorResponse showAllMajorResponse = new ShowAllMajorResponse();
            showAllMajorResponse.setMajorId(major.getMajorId());
            showAllMajorResponse.setMajorName(major.getMajorName());
            showAllMajorResponses.add(showAllMajorResponse);
        }
        PageUtil<ShowAllMajorResponse> showAllInstituteResponsePageUtil = new PageUtil<>(showAllMajorRequest.getCheckRequest().getPageRequest().getCurrentPage(), showAllMajorRequest.getCheckRequest().getPageRequest().getPageSize(), showAllMajorResponses);
        return showAllInstituteResponsePageUtil;
    }
}
