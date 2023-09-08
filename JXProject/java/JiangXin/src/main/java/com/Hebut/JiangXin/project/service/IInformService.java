package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.project.entity.Inform;
import com.Hebut.JiangXin.project.entity.request.*;
import com.Hebut.JiangXin.project.entity.response.CheckInformPageResponse;
import com.Hebut.JiangXin.project.entity.response.CheckInformResponse;
import com.Hebut.JiangXin.project.entity.response.ScanInformResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 通知表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-01
 */
public interface IInformService extends IService<Inform> {

    /**
     * 增加通知
     *
     * @param addInformRequest
     */
    void addInform(AddInformRequest addInformRequest);

    /**
     * 查看通知
     *
     * @param checkInformRequest
     * @return 通知
     */
    Page<CheckInformResponse> checkInform(CheckInformRequest checkInformRequest);

    /**
     * 增加通知
     *
     * @param scanInformRequest
     * @return 通知内容
     */
    ScanInformResponse scanInform(ScanInformRequest scanInformRequest);

    /**
     * 查看通知列表
     *
     * @param checkRequest
     * @return
     */
    CheckInformPageResponse checkAllInform(CheckRequest checkRequest);

    /**
     * 向某类用户发送通知
     *
     * @param setSomeInformRequest
     */
    void setSomeInform(SetSomeInformRequest setSomeInformRequest);

    /**
     * 删除通知
     *
     * @param deleteInformRequest
     */
    void deleteInform(DeleteInformRequest deleteInformRequest);

    /**
     * 查看自己发送通知列表
     *
     * @param checkRequest
     * @return
     */
    CheckInformPageResponse checkInformOfMine(CheckRequest checkRequest);

    Integer checkWaitReadCount(CommonRequest commonRequest);

    void oneClickRead(CommonRequest commonRequest);
}
