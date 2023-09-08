package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.ProjectScore;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckPastProjectScoreResponse;
import com.Hebut.JiangXin.project.entity.response.CheckProjectAverageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckProjectScoreResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 项目打分表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-03-07
 */
public interface IProjectScoreService extends IService<ProjectScore> {

    /**
     * 专家评分项目
     *
     * @param giveProjectScoreRequest
     */
    void giveProjectScore(GiveProjectScoreRequest giveProjectScoreRequest);

    /**
     * 查看专家未评分项目列表
     *
     * @param checkProjectScoreRequest
     * @return
     */
    PageUtil<CheckProjectScoreResponse> checkProjectScore(CheckProjectScoreRequest checkProjectScoreRequest);


    /**
     * 查看项目评分均分列表
     *
     * @param checkProjectAverageRequest
     * @return
     */
    PageUtil<CheckProjectAverageResponse> checkProjectAverage(CheckProjectAverageRequest checkProjectAverageRequest);

    /**
     * 专家提交评分项目
     *
     * @param submitProjectScoreRequest
     */
    void submitProjectScore(SubmitProjectScoreRequest submitProjectScoreRequest);

    /**
     * 查看往期评分项目列表
     *
     * @param checkPastProjectScoreRequest
     * @return
     */
    PageUtil<CheckPastProjectScoreResponse> checkPastProjectScore(CheckPastProjectScoreRequest checkPastProjectScoreRequest);
}
