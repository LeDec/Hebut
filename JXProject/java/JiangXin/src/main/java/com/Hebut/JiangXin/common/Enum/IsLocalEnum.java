package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author lidong
 */

public enum IsLocalEnum {
    /**
     * 匠心项目
     */
    OTHER("0", "其他项目"),

    /**
     * 匠心项目
     */
    LOCAL("1", "匠心项目");


    @Getter
    private final String code;

    @Getter
    private final String msg;

    IsLocalEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
