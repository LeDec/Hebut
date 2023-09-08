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
 * 学院专业关系表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InstituteMajor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学院专业关系编号
     */
    @TableField("IMrelation_Id")
    private String imrelationId;

    /**
     * 学院编号
     */
    @TableField("institute_Id")
    private String instituteId;

    /**
     * 专业编号
     */
    @TableField("major_Id")
    private String majorId;

    /**
     * 是否删除（0，“未删除”。1，“已删除”）
     */
    @TableField("IMrelation_Is_Del")
    private String imrelationIsDel;

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
