package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.ProjectUser;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 项目用户关系表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-29
 */
public interface IProjectUserService extends IService<ProjectUser> {

    /**
     * 查询结果
     *
     * @param commonRequest 查询请求
     * @return 项目结果
     * @author lidong
     */
    SearchResultResponse searchResult(CommonRequest commonRequest);

    /**
     * 查看小组成员
     *
     * @param scanProjectRequest
     * @return 小组成员
     */
    List<SearchPartnerResponse> searchPartner(ScanProjectRequest scanProjectRequest);

    /**
     * 查看小组成员
     *
     * @param commonRequest
     * @return 指导教师
     */
    List<InformationResponse> searchTeacher(CommonRequest commonRequest);

    /**
     * 查询我的项目
     *
     * @param searchMyProjectRequest
     * @return 我的项目
     */
    PageUtil<ShowProjectResponse> searchMyProject(SearchMyProjectRequest searchMyProjectRequest);

    /**
     * 我已参与的匠心项目
     *
     * @param commonRequest
     * @return
     */
    ScanProjectResponse localProject(CommonRequest commonRequest);

    /**
     * 查询我指导的项目
     *
     * @param searchGuideProjectRequest
     * @return
     */
    PageUtil<SearchGuideProjectResponse> searchGuideProject(SearchGuideProjectRequest searchGuideProjectRequest);

    /**
     * 分配专家项目审核任务
     *
     * @param distributeProjectRequest
     */
    void distributeProject(DistributeProjectRequest distributeProjectRequest);

    /**
     * 我已报名的匠心项目
     *
     * @param commonRequest
     * @return
     */
    ScanProjectResponse futureProject(CommonRequest commonRequest);
}
