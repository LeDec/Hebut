package com.android.quest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author liDong
 * @since 2023-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "_id", type = IdType.AUTO)
    private Integer id;

    private String phone;

    private String password;

    private String nickname;

    private String profile;

    private Integer coin;

    private String isCompleteDaily;

    private String isCompleteWeekly;

    private LocalDate updateTime;


}
