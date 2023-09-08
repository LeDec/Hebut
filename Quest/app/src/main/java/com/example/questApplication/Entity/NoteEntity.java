package com.example.questApplication.Entity;

public class NoteEntity {

    private int _id;

    private String title;
    private String applicant;
    private String say;

    public NoteEntity(int _id, String title, String applicant, String say) {
        this._id = _id;
        this.title = title;
        this.applicant = applicant;
        this.say = say;
    }

    public NoteEntity(){
        this._id = -1;
        this.title = "";
        this.applicant = "";
        this.say = "";
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getSay() {
        return say;
    }

    public void setSay(String say) {
        this.say = say;
    }
}
