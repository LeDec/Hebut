package com.android.quest.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AdminTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "_id", type = IdType.AUTO)
    private Integer id;

    private String userName;

    private String password;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String note;


}
