package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.project.entity.ProjectMajor;
import com.Hebut.JiangXin.project.entity.request.AddProjectMajorRelationRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目专业关系表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-12-29
 */
public interface IProjectMajorService extends IService<ProjectMajor> {

    /**
     * 测试功能
     *
     * @param addProjectMajorRequest
     */
    void addRelation(AddProjectMajorRelationRequest addProjectMajorRequest);

}
