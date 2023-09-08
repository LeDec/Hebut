package com.Hebut.JiangXin.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 批次表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Batch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 批次编号
     */
    private String batchId;

    /**
     * 批次名称
     */
    private String batchName;

    /**
     * 批次主题
     */
    private String batchTheme;

    /**
     * 征集阶段开始时间
     */
    private LocalDateTime selectBeginning;

    /**
     * 征集阶段截止时间
     */
    private LocalDateTime selectDeadline;

    /**
     * 报名阶段开始时间
     */
    private LocalDateTime enrollBeginning;

    /**
     * 报名阶段截止时间
     */
    private LocalDateTime enrollDeadline;

    /**
     * 进行时间开始时间
     */
    private LocalDateTime mediumBeginning;

    /**
     * 进行时间截止时间
     */
    private LocalDateTime mediumDeadline;

    /**
     * 进行时间开始时间
     */
    private LocalDateTime defendBeginning;

    /**
     * 进行时间截止时间
     */
    private LocalDateTime defendDeadline;

    /**
     * 创建时间
     */
    @TableField("create_Time")
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
