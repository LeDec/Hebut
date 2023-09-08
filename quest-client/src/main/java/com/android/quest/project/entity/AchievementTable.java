package com.android.quest.project.entity;

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
 * @since 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AchievementTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "_id")
    private String id;

    private Integer questId;
    private String title;

    private String type;

    private String soundRecording;


}
