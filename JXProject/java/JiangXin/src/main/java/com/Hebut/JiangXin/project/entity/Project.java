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
 * 项目表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Project implements Serializable {

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
     * 项目编号
     */
    private String projectId;

    /**
     * 项目题目
     */
    private String projectName;

    /**
     * 项目来源
     */
    private String projectOrigin;

    /**
     * 项目指导书
     */
    private String projectInstruction;

    /**
     * 项目需要人数
     */
    private Integer projectNeedNumber;

    /**
     * 是否为匠心项目（1，“匠心项目”。2，“其他项目”。）
     */
    private String projectIsLocal;

    @TableField("remaining_Amount")
    private BigDecimal remainingAmount;

    /**
     * 4:待审核 5：已经审核通过  6：审核未通过
     */
    private String stage;

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
