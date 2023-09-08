package com.Hebut.JiangXin.common.Enum;

import lombok.Getter;

public enum PathEnum {
    /**
     * URL
     */
    URl("localhost:8081/D:" +
            "/code/JXProject/resource/"),

    /**
     * 报名阶段
     */
    //FilePath("/usr/local/nginx/html/resource/");
    FilePath("D:/code/JXProject/resource/");


    @Getter
    private final String msg;

    PathEnum(String msg) {
        this.msg = msg;
    }
}
