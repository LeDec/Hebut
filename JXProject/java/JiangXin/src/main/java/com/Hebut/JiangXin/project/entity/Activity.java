package com.Hebut.JiangXin.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 活动表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 活动编号
     */
    private String activityId;

    /**
     * 批次编号
     */
    private String batchId;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动介绍
     */
    private String introduction;

    /**
     * 起始时间
     */
    private LocalDateTime beginning;

    /**
     * 终止时间
     */
    private LocalDateTime deadline;

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
