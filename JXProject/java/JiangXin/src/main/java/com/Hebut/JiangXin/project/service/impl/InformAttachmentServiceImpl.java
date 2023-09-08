package com.Hebut.JiangXin.project.service.impl;

import com.Hebut.JiangXin.common.Enum.PathEnum;
import com.Hebut.JiangXin.project.entity.InformAttachment;
import com.Hebut.JiangXin.project.entity.request.DownloadAttachmentRequest;
import com.Hebut.JiangXin.project.entity.response.DownloadResponse;
import com.Hebut.JiangXin.project.mapper.InformAttachmentMapper;
import com.Hebut.JiangXin.project.service.IInformAttachmentService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * <p>
 * 通知附件表 服务实现类
 * </p>
 *
 * @author LiDong
 * @since 2022-01-26
 */
@Service
public class InformAttachmentServiceImpl extends ServiceImpl<InformAttachmentMapper, InformAttachment> implements IInformAttachmentService {

    @Resource
    InformAttachmentMapper informAttachmentMapper;

    @Override
    public DownloadResponse downAttachment(DownloadAttachmentRequest downloadAttachmentRequest) {
        DownloadResponse downloadResponse = new DownloadResponse();
        File localPath = new File(PathEnum.FilePath.getMsg() + "img/attachment/");
        downloadResponse.setFilePath(localPath.getAbsolutePath());
        InformAttachment informAttachment = informAttachmentMapper.selectOne(
                Wrappers.lambdaQuery(InformAttachment.class)
                        .eq(InformAttachment::getRelationId, downloadAttachmentRequest.getRelationId())
        );
        downloadResponse.setFileName(informAttachment.getAttachment());
        return downloadResponse;
    }
}
