package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.common.util.PageUtil;
import com.Hebut.JiangXin.project.entity.Batch;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckBatchResponse;
import com.Hebut.JiangXin.project.entity.response.CheckTimeResponse;
import com.Hebut.JiangXin.project.entity.response.NowBatchResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 批次表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-27
 */
public interface IBatchService extends IService<Batch> {

    /**
     * 增加批次
     *
     * @param addBatchRequest
     */
    void addBatch(AddBatchRequest addBatchRequest);

    /**
     * 查看批次
     *
     * @param checkBatchRequest
     * @return
     */
    PageUtil<CheckBatchResponse> checkBatch(CheckBatchRequest checkBatchRequest);

    /**
     * 推送项目
     *
     * @param pushNotificationRequest
     */
    void pushNotification(PushNotificationRequest pushNotificationRequest);

    /**
     * 结束项目
     *
     * @param pushNotificationRequese
    void endBatch(PushNotificationRequest pushNotificationRequest);
     **/

    /**
     * 设置时间
     *
     * @param setBatchTimeRequest
     */
    void setTime(SetBatchTimeRequest setBatchTimeRequest);

    /**
     * nowBatchRequest
     *
     * @param nowBatchRequest
     * @return nowBatchResponse
     */
    NowBatchResponse nowBatch(NowBatchRequest nowBatchRequest);

    /**
     * 设置时间
     *
     * @param checkTimeRequest
     * @return
     */
    CheckTimeResponse checkTime(CheckTimeRequest checkTimeRequest);
}
