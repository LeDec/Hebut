package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;


/**
 * @author lidong
 */

public enum MajorEnum {


    /**
     * 教师
     */
    TEACHER("62", "教师");

    @Getter
    private final String code;

    @Getter
    private final String msg;

    MajorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
