package com.example.questApplication.Entity;

import java.util.ArrayList;
import java.util.List;

public class DungeonEntity {

    private int dungeon_id;

    private String dungeonTitle;
    private boolean isComplete;
    private int coin;
    private List<DungeonQuestEntity> questEntityList;

    public DungeonEntity() {
        dungeon_id = 0;
        dungeonTitle = null;
        isComplete = false;
        coin = 0;
        questEntityList = new ArrayList<>();
    }

    public DungeonEntity(int dungeon_id, String dungeonTitle, boolean isComplete, int coin, List<DungeonQuestEntity> questEntityList) {
        this.dungeon_id = dungeon_id;
        this.dungeonTitle = dungeonTitle;
        this.isComplete = isComplete;
        this.coin = coin;
        this.questEntityList = questEntityList;
    }

    public String getDungeonTitle() {
        return dungeonTitle;
    }

    public void setDungeonTitle(String dungeonTitle) {
        this.dungeonTitle = dungeonTitle;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public List<DungeonQuestEntity> getQuestEntityList() {
        return questEntityList;
    }

    public void setQuestEntityList(List<DungeonQuestEntity> questEntityList) {
        this.questEntityList = questEntityList;
    }

    public int getDungeon_id() {
        return dungeon_id;
    }

    public void setDungeon_id(int dungeon_id) {
        this.dungeon_id = dungeon_id;
    }
}
