package com.example.questApplication.Enum;

public enum QuestEnum {

    /**
     * 每日任务
     */
    DAILY("1", "每日任务"),

    /**
     * 每周任务
     */
    WEEKLY("2", "每周任务") {},
    /**
     * 成就任务
     */
    ACHIEVEMENT("3", "成就任务") {
        @Override
        public String getCode() {
            return super.getCode();
        }

        @Override
        public String getMsg() {
            return super.getMsg();
        }
    },
    DUNGEON("4", "副本任务");

    private final String code;

    private final String msg;


    QuestEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
