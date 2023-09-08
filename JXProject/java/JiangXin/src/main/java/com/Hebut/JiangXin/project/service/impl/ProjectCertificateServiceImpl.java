package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.*;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.ProjectCertificate;
import com.Hebut.JiangXin.project.entity.ProjectUser;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckAllMaterialPageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckMaterialResponse;
import com.Hebut.JiangXin.project.entity.response.CheckMaterialStageResponse;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.Hebut.JiangXin.project.mapper.ProjectCertificateMapper;
import com.Hebut.JiangXin.project.mapper.ProjectUserMapper;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.Hebut.JiangXin.project.service.IProjectCertificateService;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 项目报告上传关系表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-13
 */
@Service
public class ProjectCertificateServiceImpl extends ServiceImpl<ProjectCertificateMapper, ProjectCertificate> implements IProjectCertificateService {

    @Resource
    UserMapper userMapper;
    @Resource
    ProjectCertificateMapper ProjectCertificateMapper;
    @Resource
    ProjectUserMapper ProjectUserMapper;

    @Override
    public void submitMaterial(MaterialRequest materialRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, materialRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, materialRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您还未登录或账号在异地登录");
        }
        if (materialRequest.getMaterialId() == null || materialRequest.getProjectId() == null || materialRequest.getStage() == null || materialRequest.getType() == null) {
            throw new CustomException(ErrorEnum.INFORMATION_EMPTY.getCode(), "提交信息不全");
        }
        ProjectCertificate ProjectCertificate = new ProjectCertificate();
        String materialId = materialRequest.getStage() + "-" + materialRequest.getProjectId() + "-" + UUID.randomUUID().toString().replace("\\-", "");
        ProjectCertificate.setRelationId(materialId);
        ProjectCertificate.setCertificate(materialRequest.getMaterialId());
        String type = null;
        if (Objects.equals(materialRequest.getType(), FileTypeEnum.REPORT.getCode())) {
            type = FileTypeEnum.REPORT.getMsg();
        } else if (Objects.equals(materialRequest.getType(), FileTypeEnum.VIDEO.getCode())) {
            type = FileTypeEnum.VIDEO.getMsg();
        } else if (Objects.equals(materialRequest.getType(), FileTypeEnum.PICTURE.getCode())) {
            type = FileTypeEnum.PICTURE.getMsg();
        } else if (Objects.equals(materialRequest.getType(), FileTypeEnum.OTHER.getCode())) {
            type = FileTypeEnum.OTHER.getMsg();
        }
        ProjectCertificate.setType(type);
        ProjectCertificate.setStageId(materialRequest.getStage());
        ProjectCertificate.setProjectId(materialRequest.getProjectId());
        ProjectCertificate.setUserId(materialRequest.getCommonRequest().getUserId());
        ProjectCertificate.setLevelId(MaterialLevelEnum.AUDIT.getCode());
        ProjectCertificateMapper.insert(ProjectCertificate);
    }


    @Override
    public DownloadResponse downloadMaterial(DownloadMaterialRequest downloadMaterialRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, downloadMaterialRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, downloadMaterialRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您还未登录或账号在异地登录");
        }
        ProjectCertificate projectCertificate = ProjectCertificateMapper.selectOne(
                Wrappers.lambdaQuery(ProjectCertificate.class)
                        .eq(ProjectCertificate::getRelationId, downloadMaterialRequest.getMaterialId())
        );
        DownloadResponse downloadResponse = new DownloadResponse();
        File localPath = new File(PathEnum.FilePath.getMsg() + "img/material/");
        downloadResponse.setFilePath(localPath.getAbsolutePath());
        downloadResponse.setFileName(projectCertificate.getCertificate());
        return downloadResponse;
    }

    @Override
    public CheckMaterialStageResponse checkMaterialStage(CheckMaterialStageRequest checkMaterialStageRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkMaterialStageRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkMaterialStageRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        CheckMaterialStageResponse checkMaterialStageResponse = new CheckMaterialStageResponse();
        ProjectCertificate projectCertificate = ProjectCertificateMapper.selectOne(
                Wrappers.lambdaQuery(ProjectCertificate.class)
                        .eq(ProjectCertificate::getProjectId, checkMaterialStageRequest.getProjectId())
                        .eq(ProjectCertificate::getStageId, checkMaterialStageRequest.getMaterialStage())
        );
        if (projectCertificate == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该阶段下项目没有可查看资料");
        }
        checkMaterialStageResponse.setPCrelationId(projectCertificate.getRelationId());
        checkMaterialStageResponse.setStageId(projectCertificate.getStageId());
        checkMaterialStageResponse.setLevelId(projectCertificate.getLevelId());
        if (Objects.equals(projectCertificate.getStageId(), MaterialStageEnum.MEDIUM.getCode())) {
            checkMaterialStageResponse.setStage(MaterialStageEnum.MEDIUM.getMsg());
        } else if (Objects.equals(projectCertificate.getStageId(), MaterialStageEnum.PRE.getCode())) {
            checkMaterialStageResponse.setStage(MaterialStageEnum.PRE.getMsg());
        } else if (Objects.equals(projectCertificate.getStageId(), MaterialStageEnum.END.getCode())) {
            checkMaterialStageResponse.setStage(MaterialStageEnum.END.getMsg());
        }
        if (Objects.equals(projectCertificate.getLevelId(), MaterialLevelEnum.AUDIT.getCode())) {
            checkMaterialStageResponse.setStage(checkMaterialStageResponse.getStage() + MaterialLevelEnum.AUDIT.getMsg());
        } else if (Objects.equals(projectCertificate.getLevelId(), MaterialLevelEnum.PASS.getCode())) {
            checkMaterialStageResponse.setStage(checkMaterialStageResponse.getStage() + MaterialLevelEnum.PASS.getMsg());
        } else if (Objects.equals(projectCertificate.getLevelId(), MaterialLevelEnum.REFUSE.getCode())) {
            checkMaterialStageResponse.setStage(checkMaterialStageResponse.getStage() + MaterialLevelEnum.REFUSE.getMsg());
        }
        return checkMaterialStageResponse;
    }

    @Override
    public PageUtil<CheckMaterialResponse> checkMaterial(CheckMaterialRequest checkMaterialStageRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkMaterialStageRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkMaterialStageRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<ProjectCertificate> ProjectCertificateList = new ArrayList<>();
        ProjectCertificateList = ProjectCertificateMapper.selectList(
                Wrappers.lambdaQuery(ProjectCertificate.class)
                        .eq(ProjectCertificate::getProjectId, checkMaterialStageRequest.getProjectId())
                        .eq(ProjectCertificate::getStageId, checkMaterialStageRequest.getProjectStage())
        );
        List<CheckMaterialResponse> checkMaterialResponses = new ArrayList<>();
        for (ProjectCertificate ProjectCertificate : ProjectCertificateList) {
            CheckMaterialResponse checkMaterialResponse = new CheckMaterialResponse();
            checkMaterialResponse.setProjectMaterialId(ProjectCertificate.getRelationId());
            checkMaterialResponse.setAuditStageId(ProjectCertificate.getStageId());
            checkMaterialResponse.setAuditLevelId(ProjectCertificate.getLevelId());
            checkMaterialResponse.setFileName(ProjectCertificate.getCertificate());
            checkMaterialResponse.setFileType(ProjectCertificate.getType());

            if (Objects.equals(ProjectCertificate.getStageId(), MaterialStageEnum.MEDIUM.getCode())) {
                checkMaterialResponse.setAuditStage(MaterialStageEnum.MEDIUM.getMsg());
            } else if (Objects.equals(ProjectCertificate.getStageId(), MaterialStageEnum.PRE.getCode())) {
                checkMaterialResponse.setAuditStage(MaterialStageEnum.PRE.getMsg());
            } else if (Objects.equals(ProjectCertificate.getStageId(), MaterialStageEnum.END.getCode())) {
                checkMaterialResponse.setAuditStage(MaterialStageEnum.END.getMsg());
            }
            if (Objects.equals(ProjectCertificate.getLevelId(), MaterialLevelEnum.AUDIT.getCode())) {
                checkMaterialResponse.setAuditStage(checkMaterialResponse.getAuditStage() + MaterialLevelEnum.AUDIT.getMsg());
            } else if (Objects.equals(ProjectCertificate.getLevelId(), MaterialLevelEnum.PASS.getCode())) {
                checkMaterialResponse.setAuditStage(checkMaterialResponse.getAuditStage() + MaterialLevelEnum.PASS.getMsg());
            } else if (Objects.equals(ProjectCertificate.getLevelId(), MaterialLevelEnum.REFUSE.getCode())) {
                checkMaterialResponse.setAuditStage(checkMaterialResponse.getAuditStage() + MaterialLevelEnum.REFUSE.getMsg());
            }
            User student = userMapper.selectOne(
                    Wrappers.lambdaQuery(User.class)
                            .eq(User::getUserId, ProjectCertificate.getUserId())
            );
            checkMaterialResponse.setStudentId(student.getUserId());
            checkMaterialResponse.setStudentName(student.getUserName());
            checkMaterialResponses.add(checkMaterialResponse);
        }
        PageUtil<CheckMaterialResponse> checkMaterialResponsePage = new PageUtil<>(checkMaterialStageRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkMaterialStageRequest.getCheckRequest().getPageRequest().getPageSize(), checkMaterialResponses);
        return checkMaterialResponsePage;
    }


    @Override
    public void deleteMaterial(DeleteMaterialRequest deleteMaterialRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, deleteMaterialRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, deleteMaterialRequest.getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "没有管理员权限");
        }
        ProjectCertificate selfToken = ProjectCertificateMapper.selectOne(
                Wrappers.lambdaQuery(ProjectCertificate.class)
                        .eq(ProjectCertificate::getRelationId, deleteMaterialRequest.getProjectMaterialId())
                        .eq(ProjectCertificate::getUserId, deleteMaterialRequest.getCommonRequest().getUserId())
        );
        if (selfToken == null && !Objects.equals(token.getType(), UserTypeEnum.ADMIN.getCode())) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "没有对该项目资料的权限");
        }
        ProjectCertificate projectCertificate = ProjectCertificateMapper.selectOne(
                Wrappers.lambdaQuery(ProjectCertificate.class)
                        .eq(ProjectCertificate::getRelationId, deleteMaterialRequest.getProjectMaterialId())
        );
        if (!Objects.equals(projectCertificate.getLevelId(), MaterialLevelEnum.AUDIT.getCode())) {
            throw new CustomException(ErrorEnum.OVERDUE_ERROR.getCode(), "资料已审核无法撤销");
        } else {
            File localPath = new File(PathEnum.FilePath.getMsg() + "img/material");
            File file = new File(localPath.getAbsolutePath() + "\\" + projectCertificate.getCertificate());
            if (file.delete()) {
                ProjectCertificateMapper.deleteById(projectCertificate);
            } else {
                throw new CustomException(ErrorEnum.FILE_DELETE_ERROR.getCode(), "文件删除失败");
            }
        }

    }

    @Override
    public void auditMaterial(AuditMaterialRequest auditMaterialRequest) {
        User user = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, auditMaterialRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, auditMaterialRequest.getCommonRequest().getToken())
        );
        if (user == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "您还未登录或账号在异地登录");
        }
        ProjectCertificate projectCertificate = ProjectCertificateMapper.selectOne(
                Wrappers.lambdaQuery(ProjectCertificate.class)
                        .eq(ProjectCertificate::getRelationId, auditMaterialRequest.getMaterialId())
        );
        if (projectCertificate == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该资料不存在");
        }
        if (!Objects.equals(projectCertificate.getLevelId(), MaterialLevelEnum.AUDIT.getCode())) {
            throw new CustomException(ErrorEnum.REPEAT_OPERATION.getCode(), "该项目已经审核完成，无法重复审核更改结果");
        }
        ProjectUser projectUser = ProjectUserMapper.selectOne(
                Wrappers.lambdaQuery(ProjectUser.class)
                        .eq(ProjectUser::getProjectId, projectCertificate.getProjectId())
                        .eq(ProjectUser::getUserId, user.getUserId())
                        .eq(ProjectUser::getRelationId, ProjectUserRelationEnum.TEACHER.getCode())
        );
        if (projectUser == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "没有权限管理该项目");
        }
        if (Objects.equals(auditMaterialRequest.getIsPass(), PassEnum.PASS.getCode())) {
            projectCertificate.setLevelId(MaterialLevelEnum.PASS.getCode());
        } else {
            projectCertificate.setLevelId(MaterialLevelEnum.REFUSE.getCode());
        }
        ProjectCertificateMapper.updateById(projectCertificate);

    }

    @Override
    public CheckAllMaterialPageResponse checkAllMaterial(CheckAllMaterialRequest checkAllMaterialRequest) {
        User token = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkAllMaterialRequest.getCheckRequest().getCommonRequest().getUserId())
                        .eq(User::getToken, checkAllMaterialRequest.getCheckRequest().getCommonRequest().getToken())
        );
        if (token == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        Page page = new Page(checkAllMaterialRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAllMaterialRequest.getCheckRequest().getPageRequest().getPageSize());
        page.addOrder(OrderItem.asc("level_id"));
        page.addOrder(OrderItem.desc("stage_id"));
        Page<ProjectCertificate> ProjectCertificatePage = ProjectCertificateMapper.selectPage(
                page,
                Wrappers.lambdaQuery(ProjectCertificate.class)
                        .eq(ProjectCertificate::getProjectId, checkAllMaterialRequest.getProjectId())
        );
        Page<CheckMaterialResponse> checkMaterialResponsePage = new Page<>(checkAllMaterialRequest.getCheckRequest().getPageRequest().getCurrentPage(), checkAllMaterialRequest.getCheckRequest().getPageRequest().getPageSize());
        List<CheckMaterialResponse> checkMaterialResponses = new ArrayList<>();
        for (ProjectCertificate ProjectCertificate : ProjectCertificatePage.getRecords()) {
            CheckMaterialResponse checkMaterialResponse = new CheckMaterialResponse();
            checkMaterialResponse.setProjectMaterialId(ProjectCertificate.getRelationId());
            checkMaterialResponse.setFileName(ProjectCertificate.getCertificate());
            checkMaterialResponse.setFileType(ProjectCertificate.getType());
            checkMaterialResponse.setAuditStageId(ProjectCertificate.getStageId());
            checkMaterialResponse.setAuditLevelId(ProjectCertificate.getLevelId());
            if (Objects.equals(ProjectCertificate.getStageId(), MaterialStageEnum.MEDIUM.getCode())) {
                checkMaterialResponse.setAuditStage(MaterialStageEnum.MEDIUM.getMsg());
            } else if (Objects.equals(ProjectCertificate.getStageId(), MaterialStageEnum.PRE.getCode())) {
                checkMaterialResponse.setAuditStage(MaterialStageEnum.PRE.getMsg());
            } else if (Objects.equals(ProjectCertificate.getStageId(), MaterialStageEnum.END.getCode())) {
                checkMaterialResponse.setAuditStage(MaterialStageEnum.END.getMsg());
            }
            if (Objects.equals(ProjectCertificate.getLevelId(), MaterialLevelEnum.AUDIT.getCode())) {
                checkMaterialResponse.setAuditStage(checkMaterialResponse.getAuditStage() + MaterialLevelEnum.AUDIT.getMsg());
            } else if (Objects.equals(ProjectCertificate.getLevelId(), MaterialLevelEnum.PASS.getCode())) {
                checkMaterialResponse.setAuditStage(checkMaterialResponse.getAuditStage() + MaterialLevelEnum.PASS.getMsg());
            } else if (Objects.equals(ProjectCertificate.getLevelId(), MaterialLevelEnum.REFUSE.getCode())) {
                checkMaterialResponse.setAuditStage(checkMaterialResponse.getAuditStage() + MaterialLevelEnum.REFUSE.getMsg());
            }
            checkMaterialResponses.add(checkMaterialResponse);
        }
        checkMaterialResponsePage.setRecords(checkMaterialResponses);
        checkMaterialResponsePage.setTotal(checkMaterialResponses.size());
        long pages = ProjectCertificatePage.getTotal() / ProjectCertificatePage.getSize();
        if (ProjectCertificatePage.getSize() == 0) {
            pages = 0L;
        }
        if (ProjectCertificatePage.getTotal() % ProjectCertificatePage.getSize() != 0) {
            pages++;
        }
        CheckAllMaterialPageResponse checkAllMaterialPageResponse = new CheckAllMaterialPageResponse();
        checkAllMaterialPageResponse.setCheckMaterialResponsePage(checkMaterialResponsePage);
        checkAllMaterialPageResponse.setPages(pages);
        return checkAllMaterialPageResponse;
    }
}
