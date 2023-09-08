package com.Hebut.JiangXin.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 报销表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 报销编号
     */
    private String expenseId;

    /**
     * 申请人编号
     */
    private String applicantId;

    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 报销内容
     */
    private String introduction;

    /**
     * 报销金额
     */
    private BigDecimal amount;

    /**
     * 报销凭证
     */
    private String certificate;

    /**
     * 是否通过（0，“待审核”。1，“审核中”。2，“已通过"。3，“未通过”）
     */
    private String isPass;

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
