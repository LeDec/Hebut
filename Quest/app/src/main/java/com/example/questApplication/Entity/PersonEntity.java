package com.example.questApplication.Entity;

public class PersonEntity {
    private int _id;
    private String phone;
    private String password;
    private String nickname;
    private String profile;
    private int coin;

    public PersonEntity() {
        this._id = 1;
    }

    public PersonEntity(int _id, String phone, String password, String nickname, String profile, int coin) {
        this._id = _id;
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
        this.profile = profile;
        this.coin = coin;
    }

    /**
     * 初始化好友，不需要过多不安全数据
     *
     * @param _id
     * @param nickname
     * @param profile
     * @param coin
     */
    public PersonEntity(int _id, String nickname, String profile, int coin) {
        this._id = _id;
        this.phone = null;
        this.password = null;
        this.nickname = nickname;
        this.profile = profile;
        this.coin = coin;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

}
