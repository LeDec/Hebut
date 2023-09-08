package com.Hebut.JiangXin.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 教评表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 教评编号
     */
    private String evaluationId;

    /**
     * 学生学工号
     */
    private String studentId;

    /**
     * 老师学工号
     */
    private String teacherId;

    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 教评成绩1
     */
    private Float grade1;

    /**
     * 建议
     */
    private String suggestion1;

    /**
     * 教评成绩2
     */
    private Float grade2;

    /**
     * 建议
     */
    private String suggestion2;

    /**
     * 教评成绩3
     */
    private Float grade3;

    /**
     * 建议
     */
    private String suggestion3;

    /**
     * 教评成绩4
     */
    private Float grade4;

    /**
     * 建议
     */
    private String suggestion4;

    /**
     * 建议
     */
    private String suggestion;

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
