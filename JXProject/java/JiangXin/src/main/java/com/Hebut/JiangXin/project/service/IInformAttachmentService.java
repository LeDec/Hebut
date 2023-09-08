package com.Hebut.JiangXin.project.service;

import com.Hebut.JiangXin.project.entity.InformAttachment;
import com.Hebut.JiangXin.project.entity.request.DownloadAttachmentRequest;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 通知附件表 服务类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-26
 */
public interface IInformAttachmentService extends IService<InformAttachment> {

    /**
     * 下载通知附件
     *
     * @param downloadAttachmentRequest
     * @return
     */
    DownloadResponse downAttachment(DownloadAttachmentRequest downloadAttachmentRequest);
}
