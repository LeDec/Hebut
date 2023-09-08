package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

/**
 * @author admin
 */

public enum FileTypeEnum {

    /**
     * 报告
     */
    REPORT("0", "关键报告"),

    /**
     * 视频
     */
    VIDEO("1", "视频资料"),

    /**
     * 图片
     */
    PICTURE("2", "图片资料"),

    /**
     * 其他资料
     */
    OTHER("3", "其他资料");


    @Getter
    private final String code;

    @Getter
    private final String msg;

    FileTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
