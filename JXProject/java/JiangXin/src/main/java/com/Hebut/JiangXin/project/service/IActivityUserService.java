package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.ActivityUser;
import com.Hebut.JiangXin.project.entity.request.ActivityActivityStatisticsRequest;
import com.Hebut.JiangXin.project.entity.request.CheckActivityStudentRequest;
import com.Hebut.JiangXin.project.entity.request.CheckSignOnRequest;
import com.Hebut.JiangXin.project.entity.request.SignOnActivityRequest;
import com.Hebut.JiangXin.project.entity.response.ActivitySignStatisticsResponse;
import com.Hebut.JiangXin.project.entity.response.CheckActivitySignResponse;
import com.Hebut.JiangXin.project.entity.response.CheckActivityStudentResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 活动参与表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-02-06
 */
public interface IActivityUserService extends IService<ActivityUser> {

    /**
     * 学生签到活动
     *
     * @param signOnActivityRequest
     */
    void signOnActivity(SignOnActivityRequest signOnActivityRequest);

    /**
     * 查看活动签到列表
     *
     * @param checkSignOnRequest
     * @return
     */
    Page<CheckActivitySignResponse> checkActivitySign(CheckSignOnRequest checkSignOnRequest);

    /**
     * 统计活动签到
     *
     * @param activityActivityStatisticsRequest
     * @return
     */
    Page<ActivitySignStatisticsResponse> activityActivityStatistics(ActivityActivityStatisticsRequest activityActivityStatisticsRequest);

    /**
     * 查看活动学生列表
     *
     * @param checkActivityStudentRequest
     * @return
     */
    Page<CheckActivityStudentResponse> checkActivityStudent(CheckActivityStudentRequest checkActivityStudentRequest);
}
