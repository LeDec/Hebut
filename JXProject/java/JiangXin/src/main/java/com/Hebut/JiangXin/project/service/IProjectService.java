package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Project;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-07
 */
public interface IProjectService extends IService<Project> {
    /**
     * 显示项目列表
     *
     * @param showProjectRequest 搜索班级文章请求
     * @return 项目列表
     */
    ShowProjectResponsePage showAllProject(ShowProjectRequest showProjectRequest);

    /**
     * 增加项目
     *
     * @param addProjectRequest
     */
    void addProject(AddProjectRequest addProjectRequest);

    /**
     * 搜索项目
     *
     * @param searchProjectRequest
     * @return 项目列表
     */
    PageUtil<ShowProjectResponse> searchProject(SearchProjectRequest searchProjectRequest);

    /**
     * 查看项目内容
     *
     * @param scanProjectRequest
     * @return 项目内容
     */
    ScanProjectResponse scanProject(ScanProjectRequest scanProjectRequest);

    /**
     * 下载计划书
     *
     * @param downloadProspectusRequest
     * @return
     */
    DownloadResponse downloadProspectus(DownloadProspectusRequest downloadProspectusRequest);

    /**
     * 审核项目
     *
     * @param auditProjectRequest
     */
    void auditProject(AuditProjectRequest auditProjectRequest);

    /**
     * 显示非匠心项目
     *
     * @param showAllOtherProjectRequest
     * @return
     */
    Page<ShowAllOtherProjectResponse> showAllOtherProject(ShowAllOtherProjectRequest showAllOtherProjectRequest);

    /**
     * 删除项目
     *
     * @param deleteProjectRequest
     */
    void deleteProject(DeleteProjectRequest deleteProjectRequest);

    /**
     * 显示初试已通过项目
     *
     * @param showPassProjectRequest
     * @return
     */
    PageUtil<ShowPassProjectResponse> showPassProject(ShowPassProjectRequest showPassProjectRequest);

    /**
     * 分配项目编号
     *
     * @param addProjectDesignationRequest
     */
    void addProjectDesignation(AddProjectDesignationRequest addProjectDesignationRequest);

    /**
     * 显示初试待审核项目
     *
     * @param showAuditProjectRequest
     * @return
     */
    PageUtil<ShowAuditProjectResponse> showAuditProject(ShowAuditProjectRequest showAuditProjectRequest);

    /**
     * 项目阶段进行
     *
     * @param processProjectRequest
     */
    void processProject(ProcessProjectRequest processProjectRequest);

    /**
     * 下载项目计划书模板
     *
     * @param downloadTemplateRequest
     * @return
     */
    DownloadResponse downloadTemplate(DownloadTemplateRequest downloadTemplateRequest);
}
