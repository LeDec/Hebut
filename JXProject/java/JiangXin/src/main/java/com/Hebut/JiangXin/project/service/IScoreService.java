package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Score;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-29
 */
public interface IScoreService extends IService<Score> {

    /**
     * 查询本人成绩
     *
     * @param checkOwnScoreRequest 查询请求
     * @return 成绩信息
     * @author lidong
     */
    SearchScoreResponse checkOwnScore(CheckOwnScoreRequest checkOwnScoreRequest);

    /**
     * 查看项目学生成绩
     *
     * @param searchAllScoreRequest
     * @return
     */
    Page<CheckStudentScoreResponse> checkStudentScore(SearchAllScoreRequest searchAllScoreRequest);

    /**
     * 修改学生成绩
     *
     * @param modifyScoreRequest
     */
    void modifyScore(ModifyScoreRequest modifyScoreRequest);

    /**
     * 给定学生平时成绩
     *
     * @param giveScoreRequest
     */
    void giveScore(GiveScoreRequest giveScoreRequest);

    /**
     * 设置分数占比
     *
     * @param setStandardRequest
     */
    void setStandard(SetStandardRequest setStandardRequest);


    /**
     * 权限修改学生成绩
     *
     * @param modifyScoreRequestPromoteRequest
     */
    void modifyScorePromote(ModifyScoreRequestPromoteRequest modifyScoreRequestPromoteRequest);


    /**
     * 查看匠心班学生成绩
     *
     * @param checkAllScoreRequest
     * @return
     */
    PageUtil<SearchAllScoreResponse> checkAllScore(CheckAllScoreRequest checkAllScoreRequest);

    /**
     * 显示各项目答辩成绩
     *
     * @param showAllDefenseScoreRequest
     * @return
     */
    ShowAllDefenseScoreResponsePage showAllDefenseScore(ShowAllDefenseScoreRequest showAllDefenseScoreRequest);

    /**
     * 查看分数占比
     *
     * @param commonRequest
     * @return
     */
    CheckStandardResponse checkStandard(CommonRequest commonRequest);

    /**
     * 查看各项成绩学生总名单
     *
     * @param checkScoreRequest
     * @return
     */
    PageUtil<CheckScoreResponse> checkScore(CheckScoreRequest checkScoreRequest);

    /**
     * 查看各项成绩未赋分学生总名单
     *
     * @param checkScoreRequest
     * @return
     */
    PageUtil<CheckRemainScoreResponse> checkRemainScore(CheckScoreRequest checkScoreRequest);

    /**
     * 给定学生答辩成绩
     *
     * @param giveDefenseRequest
     */
    void giveDefense(GiveDefenseRequest giveDefenseRequest);

    /**
     * 查看活动成绩未赋分学生总名单
     *
     * @param checkScoreRequest
     * @return
     */
    PageUtil<StudentSignStatisticsResponse> checkRemainActivityScore(CheckScoreRequest checkScoreRequest);

    /**
     * 提交学生成绩
     *
     * @param submitScoreRequest
     */
    void submitScore(SubmitScoreRequest submitScoreRequest);

    /**
     * 提交学生答辩成绩
     *
     * @param submitDefenseRequest
     */
    void submitDefense(SubmitDefenseRequest submitDefenseRequest);

    /**
     * 查看指导项目成绩
     *
     * @param checkGuideScoreRequest
     * @return
     */
    PageUtil<SearchAllScoreResponse> checkGuideScore(CheckGuideScoreRequest checkGuideScoreRequest);

    /**
     * 查看教师指导项目学生成绩总名单
     *
     * @param checkScoreRequest
     * @return
     */
    PageUtil<CheckScoreResponse> checkMyGuideUsualScore(CheckScoreRequest checkScoreRequest);
}
