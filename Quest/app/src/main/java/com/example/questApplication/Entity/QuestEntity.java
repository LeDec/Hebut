package com.example.questApplication.Entity;

public class QuestEntity {

    private int quest_id;
    private String questTitle;
    private boolean isComplete;
    private int combo;
    private int coin;
    private String selfTreasure;
    private String questType;

    public QuestEntity(){
        this.quest_id = 0;
        this.questTitle = "";
        this.isComplete = false;
        this.combo = 0;
        this.coin = 0;
        this.selfTreasure = "";
        this.questType = "";
    }

    public QuestEntity(int id, String questTitle, boolean isComplete, int combo, int coin, String selfTreasure, String questType) {
        this.quest_id = id;
        this.questTitle = questTitle;
        this.isComplete = isComplete;
        this.combo = combo;
        this.coin = coin;
        this.selfTreasure = selfTreasure;
        this.questType = questType;
    }

    public String getQuestTitle() {
        return questTitle;
    }

    public void setQuestTitle(String questTitle) {
        this.questTitle = questTitle;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getSelfTreasure() {
        return selfTreasure;
    }

    public void setSelfTreasure(String selfTreasure) {
        this.selfTreasure = selfTreasure;
    }

    public String getQuestType() {
        return questType;
    }

    public void setQuestType(String questType) {
        this.questType = questType;
    }

    public int getQuest_id() {
        return quest_id;
    }

    public void setQuest_id(int id) {
        this.quest_id = id;
    }
}
