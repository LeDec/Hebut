package com.android.quest.project.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class QuestInforTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "_id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer questId;

    private LocalDate createTime;


}
