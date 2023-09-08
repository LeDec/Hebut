package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.ErrorEnum;
import com.Hebut.JiangXin.common.Enum.UserTypeEnum;
import com.Hebut.JiangXin.common.exception.CustomException;
import com.Hebut.JiangXin.project.entity.Batch;
import com.Hebut.JiangXin.project.entity.BatchProject;
import com.Hebut.JiangXin.project.entity.Project;
import com.Hebut.JiangXin.project.entity.User;
import com.Hebut.JiangXin.project.entity.request.CheckProjectTimeRequest;
import com.Hebut.JiangXin.project.entity.request.IsNowBatchRequest;
import com.Hebut.JiangXin.project.entity.request.SetProjectTimeRequest;
import com.Hebut.JiangXin.project.entity.response.CheckProjectTimeResponse;
import com.Hebut.JiangXin.project.mapper.BatchMapper;
import com.Hebut.JiangXin.project.mapper.BatchProjectMapper;
import com.Hebut.JiangXin.project.mapper.ProjectMapper;
import com.Hebut.JiangXin.project.mapper.UserMapper;
import com.Hebut.JiangXin.project.service.IBatchProjectService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 批次项目表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-27
 */
@Service
public class BatchProjectServiceImpl extends ServiceImpl<BatchProjectMapper, BatchProject> implements IBatchProjectService {

    @Resource
    BatchMapper batchMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    BatchProjectMapper batchProjectMapper;
    @Resource
    ProjectMapper projectMapper;

    @Override
    public void isNowBatch(IsNowBatchRequest isNowBatchRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, isNowBatchRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, isNowBatchRequest.getCommonRequest().getToken())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "验证失败");
        }
        List<Batch> batchList = batchMapper.selectList(
                Wrappers.lambdaQuery(Batch.class)
        );
        if (batchList.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "暂无批次");
        }
        Batch batch = batchList.get(0);
        for (int i = 1; i < batchList.size(); i++) {
            if (batchList.get(i).getDefendDeadline().isAfter(batchList.get(i - 1).getDefendDeadline())) {
                batch = batchList.get(i);
            }
        }
        BatchProject batchProject = batchProjectMapper.selectOne(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, batch.getBatchId())
                        .eq(BatchProject::getProjectId, isNowBatchRequest.getProjectId())
        );
        if (batchProject == null) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "该项目不属于当前批次");
        }
    }

    @Override
    public void setProjectTime(SetProjectTimeRequest setProjectTimeRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, setProjectTimeRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, setProjectTimeRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        Batch batch = batchMapper.selectOne(
                Wrappers.lambdaQuery(Batch.class)
                        .eq(Batch::getBatchId, setProjectTimeRequest.getBatchId())
        );
        List<BatchProject> batchProjectList = batchProjectMapper.selectList(
                Wrappers.lambdaQuery(BatchProject.class)
                        .eq(BatchProject::getBatchId, batch.getBatchId())
        );
        if (batchProjectList.size() == 0) {
            throw new CustomException(ErrorEnum.NOT_FOUND_ERROR.getCode(), "批次下没有项目");
        }
        for (BatchProject batchProject : batchProjectList) {
            Project project = projectMapper.selectOne(
                    Wrappers.lambdaQuery(Project.class)
                            .eq(Project::getProjectId, batchProject.getProjectId())
            );
            projectMapper.updateById(project);
        }
    }

    @Override
    public CheckProjectTimeResponse checkProjectTime(CheckProjectTimeRequest checkProjectTimeRequest) {
        User admin = userMapper.selectOne(
                Wrappers.lambdaQuery(User.class)
                        .eq(User::getUserId, checkProjectTimeRequest.getCommonRequest().getUserId())
                        .eq(User::getToken, checkProjectTimeRequest.getCommonRequest().getToken())
                        .eq(User::getType, UserTypeEnum.ADMIN.getCode())
        );
        if (admin == null) {
            throw new CustomException(ErrorEnum.TOKEN_WRONG.getCode(), "管理员验证失败");
        }
        return new CheckProjectTimeResponse();
    }
}
