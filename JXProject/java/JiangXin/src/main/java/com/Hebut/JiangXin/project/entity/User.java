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
 * 用户表
 * </p>
 *
 * @author LiDong
 * @since 2022-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户账号
     */
    private String userId;

    /**
     * 用户密码
     */
    private String userPasswords;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户类型
     */
    private String type;

    /**
     * 用户学院编号
     */
    private String instituteId;

    /**
     * 用户专业编号
     */
    private String majorId;

    /**
     * 用户邮箱
     */
    private String eMail;

    /**
     * token验证
     */
    private String token;

    /**
     * 是否审核通过
     */
    private String isAudit;
    /**
     * 是否拥有特殊权限（0，不拥有；1，拥有）
     */
    private String isPromote;

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
