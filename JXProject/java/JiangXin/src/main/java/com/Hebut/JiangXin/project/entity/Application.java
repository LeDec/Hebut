package com.Hebut.JiangXin.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 报名表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 报名编号
     */
    private String applicationId;

    /**
     * 批次编号
     */
    private String batchId;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 报名状态（ 1、已申请；2、初试已通过；3、 复试已通过；4、 初试未通过；5、复试未通过\n）
     */
    private String relationId;

    /**
     * 凭证
     */
    private String certificate;

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
