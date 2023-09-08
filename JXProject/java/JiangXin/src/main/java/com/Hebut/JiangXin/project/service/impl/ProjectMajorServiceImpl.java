package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.project.entity.Major;
import com.Hebut.JiangXin.project.entity.Project;
import com.Hebut.JiangXin.project.entity.ProjectMajor;
import com.Hebut.JiangXin.project.entity.request.AddProjectMajorRelationRequest;
import com.Hebut.JiangXin.project.mapper.MajorMapper;
import com.Hebut.JiangXin.project.mapper.ProjectMajorMapper;
import com.Hebut.JiangXin.project.mapper.ProjectMapper;
import com.Hebut.JiangXin.project.service.IProjectMajorService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 * 项目专业关系表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-29
 */
@Service
public class ProjectMajorServiceImpl extends ServiceImpl<ProjectMajorMapper, ProjectMajor> implements IProjectMajorService {

    @Resource
    ProjectMajorMapper projectMajorMapper;
    @Resource
    ProjectMapper projectMapper;
    @Resource
    MajorMapper majorMapper;

    @Override
    public void addRelation(AddProjectMajorRelationRequest addProjectMajorRequest) {
        Project project = projectMapper.selectOne(
                Wrappers.lambdaQuery(Project.class)
                        .eq(Project::getProjectId, addProjectMajorRequest.getProjectId())
        );
        if (project == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "项目不存在");
        }
        Major major = majorMapper.selectOne(
                Wrappers.lambdaQuery(Major.class)
                        .eq(Major::getMajorId, addProjectMajorRequest.getMajorId())
        );
        if (major == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "专业不存在");
        }
        ProjectMajor ProjectMajor = new ProjectMajor();
        String relationId = "PM-" + UUID.randomUUID().toString().replace("\\-", "");
        ProjectMajor.setRelationId(relationId);
        ProjectMajor.setMajorId(addProjectMajorRequest.getMajorId());
        ProjectMajor.setProjectId(addProjectMajorRequest.getProjectId());
        projectMajorMapper.insert(ProjectMajor);
    }
}
