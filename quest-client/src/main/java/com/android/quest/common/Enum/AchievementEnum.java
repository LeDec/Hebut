package com.android.quest.common.Enum;

import lombok.Getter;

public enum AchievementEnum {

    QUEST(0, "quest"),


    DUNGEON(1, "dungeon");

    @Getter
    private final int code;
    /**
     * 错误信息
     */
    @Getter
    private final String msg;

    /**
     * 构造函数
     */
    AchievementEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
