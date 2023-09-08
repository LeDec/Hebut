package com.Hebut.JiangXin.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 项目报告上传关系表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectCertificate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目报告编号
     */
    private String relationId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 凭证文件名
     */
    private String certificate;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 项目阶段（1，中期报告；2，答辩报告；3，结项报告）
     */
    private String stageId;

    /**
     * 审核状态（1，待审核；2，已通过；3，未通过）
     */
    private String levelId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 系统备注
     */
    private String note;


}
