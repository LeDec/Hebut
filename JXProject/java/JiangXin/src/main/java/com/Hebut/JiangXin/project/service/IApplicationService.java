package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Application;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckEnrollResponse;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.Hebut.JiangXin.project.entity.response.ShowAllApplicantResponse;
import com.Hebut.JiangXin.project.entity.response.ShowAllApplicationResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 报名表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-06
 */
public interface IApplicationService extends IService<Application> {

    /**
     * 报名
     *
     * @param enrollRequest
     */
    void enroll(EnrollRequest enrollRequest);

    /**
     * 查看报名列表
     *
     * @param checkAuditEnrollRequest
     * @return 报名列表
     */
    Page<CheckEnrollResponse> checkAuditEnroll(CheckAuditEnrollRequest checkAuditEnrollRequest);

    /**
     * 撤销报名
     *
     * @param cancelEnrollRequest
     */
    void cancelEnroll(CancelEnrollRequest cancelEnrollRequest);

    /**
     * 审核初始报名
     *
     * @param auditApplicationRequest
     */
    void auditApplication(AuditApplicationRequest auditApplicationRequest);

    /**
     * 下载报名文件
     *
     * @param downloadApplicationRequest
     * @return
     */
    DownloadResponse downloadApplication(DownloadApplicationRequest downloadApplicationRequest);

    /**
     * 搜索审核报名表
     *
     * @param searchAuditEnrollRequest
     * @return
     */
    Page<CheckEnrollResponse> searchAuditEnroll(SearchAuditEnrollRequest searchAuditEnrollRequest);

    /**
     * 复试审核
     *
     * @param auditApplicationRequest
     */
    void auditRetest(AuditApplicationRequest auditApplicationRequest);

    /**
     * 查看复试审核报名表
     *
     * @param checkAuditEnrollRequest
     * @return
     */
    PageUtil<CheckEnrollResponse> checkAuditRetest(CheckAuditEnrollRequest checkAuditEnrollRequest);

    /**
     * 显示所有可报名项目
     *
     * @param showAllApplicationRequest
     * @return
     */
    Page<ShowAllApplicationResponse> showAllApplicationProject(ShowAllApplicationRequest showAllApplicationRequest);

    /**
     * 显示所有已报名成员
     *
     * @param showAllApplicantRequest
     * @return
     */
    Page<ShowAllApplicantResponse> showAllApplicant(ShowAllApplicantRequest showAllApplicantRequest);

    /**
     * 搜索复试审核报名表
     *
     * @param searchAuditEnrollRequest
     * @return
     */
    PageUtil<CheckEnrollResponse> searchAuditRetest(SearchAuditEnrollRequest searchAuditEnrollRequest);

    /**
     * 下载项目计划书模板
     *
     * @param downloadTemplateRequest
     * @return
     */
    DownloadResponse downloadApplicationTemplate(DownloadTemplateRequest downloadTemplateRequest);
}
