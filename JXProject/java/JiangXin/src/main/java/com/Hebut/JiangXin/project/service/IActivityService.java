package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.project.entity.Activity;
import com.Hebut.JiangXin.project.entity.request.AddActivityRequest;
import com.Hebut.JiangXin.project.entity.request.CheckActivityRequest;
import com.Hebut.JiangXin.project.entity.request.DeleteActivityRequest;
import com.Hebut.JiangXin.project.entity.response.CheckActivityResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-02-06
 */
public interface IActivityService extends IService<Activity> {

    /**
     * 增加活动
     *
     * @param addActivityRequest
     */
    void addActivity(AddActivityRequest addActivityRequest);

    /**
     * 查看活动列表
     *
     * @param checkActivityRequest
     * @return
     */
    Page<CheckActivityResponse> checkActivity(CheckActivityRequest checkActivityRequest);

    /**
     * 删除活动
     *
     * @param deleteActivityRequest
     */
    void deleteActivity(DeleteActivityRequest deleteActivityRequest);
}
