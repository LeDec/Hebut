package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Institute;
import com.Hebut.JiangXin.project.entity.request.AddInstituteRequest;
import com.Hebut.JiangXin.project.entity.request.ShowAllInstituteRequest;
import com.Hebut.JiangXin.project.entity.response.ShowAllInstituteResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学院表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
public interface IInstituteService extends IService<Institute> {

    /**
     * 增加学院
     *
     * @param addInstituteRequest
     */
    void addInstitute(AddInstituteRequest addInstituteRequest);

    /**
     * 显示所有学院
     *
     * @param showAllInstituteRequest
     * @return
     */
    PageUtil<ShowAllInstituteResponse> showAllInstitute(ShowAllInstituteRequest showAllInstituteRequest);
}
