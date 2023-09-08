package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.ProjectCertificate;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckAllMaterialPageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckMaterialResponse;
import com.Hebut.JiangXin.project.entity.response.CheckMaterialStageResponse;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目报告上传关系表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-13
 */
public interface IProjectCertificateService extends IService<ProjectCertificate> {

    /**
     * 上传资料
     *
     * @param materialRequest
     */

    void submitMaterial(MaterialRequest materialRequest);

    /**
     * 下载文件资料
     *
     * @param downloadMaterialRequest
     * @return
     */
    DownloadResponse downloadMaterial(DownloadMaterialRequest downloadMaterialRequest);

    /**
     * 查看提交资料状态
     *
     * @param checkMaterialStageRequest
     * @return
     */
    CheckMaterialStageResponse checkMaterialStage(CheckMaterialStageRequest checkMaterialStageRequest);

    /**
     * 查看项目已提交资料
     *
     * @param checkMaterialStageRequest
     * @return
     */
    PageUtil<CheckMaterialResponse> checkMaterial(CheckMaterialRequest checkMaterialStageRequest);

    /**
     * 删除提交资料
     *
     * @param deleteMaterialRequest
     */
    void deleteMaterial(DeleteMaterialRequest deleteMaterialRequest);

    /**
     * 审核资料
     *
     * @param auditMaterialRequest
     */
    void auditMaterial(AuditMaterialRequest auditMaterialRequest);

    /**
     * 查看项目所有已提交资料
     *
     * @param checkAllMaterialRequest
     * @return
     */
    CheckAllMaterialPageResponse checkAllMaterial(CheckAllMaterialRequest checkAllMaterialRequest);
}
