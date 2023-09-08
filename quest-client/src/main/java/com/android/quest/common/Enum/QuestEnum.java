package com.android.quest.common.Enum;

import lombok.Getter;

public enum QuestEnum {

    /**
     * 每日任务
     */
    DAILY("1", "daily"),

    /**
     * 每周任务
     */
    WEEKLY("2", "weekly") ,
    /**
     * 成就任务
     */
    ACHIEVEMENT("3", "achievement"),
    DUNGEON("4","dungeon");

    @Getter
    private final String code;
    @Getter
    private final String msg;


    QuestEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
