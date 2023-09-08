package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Major;
import com.Hebut.JiangXin.project.entity.request.AddMajorRequest;
import com.Hebut.JiangXin.project.entity.request.ShowAllMajorRequest;
import com.Hebut.JiangXin.project.entity.response.ShowAllMajorResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 专业表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2021-11-26
 */
public interface IMajorService extends IService<Major> {
    /**
     * 增加专业
     *
     * @param addMajorRequest
     */
    void addMajor(AddMajorRequest addMajorRequest);

    /**
     * 显示所有专业
     *
     * @param showAllMajorRequest
     * @return
     */
    PageUtil<ShowAllMajorResponse> showAllMajor(ShowAllMajorRequest showAllMajorRequest);
}
