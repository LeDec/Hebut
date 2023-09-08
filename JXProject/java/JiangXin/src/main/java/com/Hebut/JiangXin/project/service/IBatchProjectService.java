package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.project.entity.BatchProject;
import com.Hebut.JiangXin.project.entity.request.CheckProjectTimeRequest;
import com.Hebut.JiangXin.project.entity.request.IsNowBatchRequest;
import com.Hebut.JiangXin.project.entity.request.SetProjectTimeRequest;
import com.Hebut.JiangXin.project.entity.response.CheckProjectTimeResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 批次项目表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-27
 */
public interface IBatchProjectService extends IService<BatchProject> {

    /**
     * 判断该项目是否为本期项目
     *
     * @param isNowBatchRequest
     */
    void isNowBatch(IsNowBatchRequest isNowBatchRequest);

    /**
     * 设置项目时间
     *
     * @param setProjectTimeRequest
     */
    void setProjectTime(SetProjectTimeRequest setProjectTimeRequest);

    /**
     * 查看项目时间
     *
     * @param checkProjectTimeRequest
     * @return
     */
    CheckProjectTimeResponse checkProjectTime(CheckProjectTimeRequest checkProjectTimeRequest);
}
