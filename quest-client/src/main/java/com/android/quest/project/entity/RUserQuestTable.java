package com.android.quest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

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
@TableName("R_user_quest_table")
public class RUserQuestTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "_id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer questId;

    private Integer combo;

    private String isComplete;

    private LocalDate updateTime;


}
