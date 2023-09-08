package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Evaluation;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckAdviceResponse;
import com.Hebut.JiangXin.project.entity.response.CheckOwnEvaluationResponse;
import com.Hebut.JiangXin.project.entity.response.CheckRankResponse;
import com.Hebut.JiangXin.project.entity.response.EvaluationStageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 教评表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-04
 */
public interface IEvaluationService extends IService<Evaluation> {

    /**
     * 填写教评
     *
     * @param addEvaluationRequest
     */

    void fillEvaluation(AddEvaluationRequest addEvaluationRequest);

    /**
     * 查看教评排名
     *
     * @param checkRankRequest
     * @return
     */
    PageUtil<CheckRankResponse> checkRank(CheckRankRequest checkRankRequest);

    /**
     * 查看教师教评意见
     *
     * @param checkAdviceRequest
     * @return
     */
    PageUtil<CheckAdviceResponse> checkAdvice(CheckAdviceRequest checkAdviceRequest);

    /**
     * 发布教评
     *
     * @param publishEvaluationRequest
     */
    void publishEvaluation(PublishEvaluationRequest publishEvaluationRequest);


    /**
     * 查询教评状态
     *
     * @param evaluationStageRequest
     * @return
     */
    PageUtil<EvaluationStageResponse> evaluationStage(EvaluationStageRequest evaluationStageRequest);

    /**
     * 查询自己教评
     *
     * @param checkOwnEvaluationRequest
     * @return
     */
    PageUtil<CheckOwnEvaluationResponse> checkOwnEvaluation(CheckOwnEvaluationRequest checkOwnEvaluationRequest);

    /**
     * 修改教评时间
     *
     * @param changeEvaluationTimeRequest
     */
    void changeEvaluationTime(ChangeEvaluationTimeRequest changeEvaluationTimeRequest);
}
