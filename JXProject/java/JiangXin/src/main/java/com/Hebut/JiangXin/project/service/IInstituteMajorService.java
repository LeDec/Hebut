package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.project.entity.InstituteMajor;
import com.Hebut.JiangXin.project.entity.request.AddInstituteMajorRelationRequest;
import com.Hebut.JiangXin.project.entity.request.ShowMajorRequest;
import com.Hebut.JiangXin.project.entity.response.ShowMajorResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学院专业关系表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
public interface IInstituteMajorService extends IService<InstituteMajor> {
    /**
     * 增加关系
     *
     * @param addInstituteMajorRequest
     */
    void addRelation(AddInstituteMajorRelationRequest addInstituteMajorRequest);

    /**
     * 查看该学院下的专业
     *
     * @param showMajorRequest
     * @return
     */
    List<ShowMajorResponse> showMajor(ShowMajorRequest showMajorRequest);
}
