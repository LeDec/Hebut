package com.example.questApplication.Entity;

public class DungeonQuestEntity {

    private int quest_id;
    private String quest_title;
    private String quest_type;

    public int getQuest_id() {
        return quest_id;
    }

    public void setQuest_id(int quest_id) {
        this.quest_id = quest_id;
    }


    public String getQuest_title() {
        return quest_title;
    }

    public void setQuest_title(String quest_title) {
        this.quest_title = quest_title;
    }

    public String getQuest_type() {
        return quest_type;
    }

    public void setQuest_type(String quest_type) {
        this.quest_type = quest_type;
    }
}
