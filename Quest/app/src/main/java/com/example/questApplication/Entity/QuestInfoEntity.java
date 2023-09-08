package com.example.questApplication.Entity;

import java.time.LocalDate;

public class QuestInfoEntity {

    private int _id;

    private int questId;

    private String title;

    private String type;

    private String createTime;

    public QuestInfoEntity(int _id,int questId,String title,String type,String createTime){
        this._id = _id;
        this.questId = questId;
        this.title = title;
        this.type = type;
        this.createTime = createTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getQuestId() {
        return questId;
    }

    public void setQuestId(int questId) {
        this.questId = questId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
