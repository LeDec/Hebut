package com.Hebut.JiangXin.project.service.impl;


import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.project.entity.Institute;
import com.Hebut.JiangXin.project.entity.InstituteMajor;
import com.Hebut.JiangXin.project.entity.Major;
import com.Hebut.JiangXin.project.entity.request.AddInstituteMajorRelationRequest;
import com.Hebut.JiangXin.project.entity.request.ShowMajorRequest;
import com.Hebut.JiangXin.project.entity.response.ShowMajorResponse;
import com.Hebut.JiangXin.project.mapper.InstituteMajorMapper;
import com.Hebut.JiangXin.project.mapper.InstituteMapper;
import com.Hebut.JiangXin.project.mapper.MajorMapper;
import com.Hebut.JiangXin.project.service.IInstituteMajorService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 学院专业关系表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
@Service
public class InstituteMajorServiceImpl extends ServiceImpl<InstituteMajorMapper, InstituteMajor> implements IInstituteMajorService {
    @Resource
    InstituteMajorMapper instituteMajorMapper;
    @Resource
    InstituteMapper instituteMapper;
    @Resource
    MajorMapper majorMapper;

    @Override
    public void addRelation(AddInstituteMajorRelationRequest addInstituteMajorRequest) {
        Institute institute = instituteMapper.selectOne(
                Wrappers.lambdaQuery(Institute.class)
                        .eq(Institute::getInstituteId, addInstituteMajorRequest.getInstituteId())
        );
        if (institute == null) {
            throw new CustomException("学院不存在");
        }
        Major major = majorMapper.selectOne(
                Wrappers.lambdaQuery(Major.class)
                        .eq(Major::getMajorId, addInstituteMajorRequest.getMajorId())
        );
        if (major == null) {
            throw new CustomException("专业不存在");
        }
        InstituteMajor InstituteMajor = new InstituteMajor();
        String relationId = "IM-" + UUID.randomUUID().toString().replace("\\-", "");
        InstituteMajor.setImrelationId(relationId);
        InstituteMajor.setInstituteId(addInstituteMajorRequest.getInstituteId());
        InstituteMajor.setMajorId(addInstituteMajorRequest.getMajorId());
        instituteMajorMapper.insert(InstituteMajor);
    }

    @Override
    public List<ShowMajorResponse> showMajor(ShowMajorRequest showMajorRequest) {
        List<InstituteMajor> InstituteMajorList = instituteMajorMapper.selectList(
                Wrappers.lambdaQuery(InstituteMajor.class)
                        .eq(InstituteMajor::getInstituteId, showMajorRequest.getInstituteId())
        );
        if (InstituteMajorList.size() <= 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "学院不存在");
        }
        List<ShowMajorResponse> showMajorResponseList = new ArrayList<>();
        for (InstituteMajor InstituteMajor : InstituteMajorList) {
            ShowMajorResponse showMajorResponse = new ShowMajorResponse();
            Major major = majorMapper.selectOne(
                    Wrappers.lambdaQuery(Major.class)
                            .eq(Major::getMajorId, InstituteMajor.getMajorId())
            );
            if (major == null) {
                throw new CustomException(ErrorEnum.DATABASE_WRONG.getCode(), "数据库专业缺失");
            }
            showMajorResponse.setMajorName(major.getMajorName());
            showMajorResponse.setMajorId(major.getMajorId());
            showMajorResponseList.add(showMajorResponse);
        }
        return showMajorResponseList;
    }
}
