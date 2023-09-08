package com.example.questApplication.Util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questApplication.Entity.DungeonEntity;
import com.example.questApplication.Entity.PersonEntity;
import com.example.questApplication.Entity.QuestEntity;
import com.example.questApplication.Enum.QuestEnum;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteUtils extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private String sql;


    public SQLiteUtils(Context context) {
        super(context, "quest_data", null, 1);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS quest_table (" +
                "    _id           INTEGER       PRIMARY KEY AUTOINCREMENT," +
                "    title         VARCHAR (255) NOT NULL," +
                "    coin          INT           NOT NULL" +
                "                                DEFAULT '0'," +
                "    self_treasure VARCHAR (255)," +
                "    type          TEXT          CHECK (type IN ('daily', 'weekly', 'achievement') ) " +
                "                                NOT NULL" +
                "                                DEFAULT 'daily');"
        );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS user_table (" +
                "    _id      INTEGER       PRIMARY KEY AUTOINCREMENT" +
                "                           DEFAULT (10000)," +
                "    phone    VARCHAR (11)  UNIQUE," +
                "    password VARCHAR (255) NOT NULL," +
                "    nickname VARCHAR (255) NOT NULL," +
                "    profile  VARCHAR (255) NOT NULL," +
                "    coin     INT           NOT NULL" +
                "                           DEFAULT 0);"
        );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS achievement_table (" +
                "    quest_id        INTEGER       PRIMARY KEY" +
                "                                  REFERENCES quest_table (_id) ON DELETE NO ACTION," +
                "    title           VARCHAR (255) NOT NULL," +
                "    type            VARCAHR (255) CHECK (type IN ('daily', 'weekly', 'achievement') ) " +
                "                                  NOT NULL," +
                "    sound_recording VARCHAR (255) );"
        );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS dungeon_table (" +
                "    _id   INTEGER       PRIMARY KEY AUTOINCREMENT," +
                "    title VARCAHR (255) NOT NULL," +
                "    coin  INTEGER       NOT NULL DEFAULT (0));"
        );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS R_friends_table (" +
                "    user_id   INTEGER REFERENCES user_table (_id)," +
                "    friend_id INTEGER REFERENCES user_table (_id)," +
                "    PRIMARY KEY (" +
                "        user_id," +
                "        friend_id" +
                "    ));"
        );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS R_user_achievement_table (" +
                "    user_id        INTEGER PRIMARY KEY" +
                "                           REFERENCES user_table (_id)," +
                "    achievement_id INTEGER REFERENCES achievement_table (quest_id) " +
                ");"
        );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS R_user_quest_table (" +
                "    user_id     INTEGER PRIMARY KEY" +
                "                        REFERENCES user_table (_id)," +
                "    quest_id    INTEGER REFERENCES quest_table (_id) " +
                "                        NOT NULL," +
                "    combo       INTEGER DEFAULT (0) " +
                "                        NOT NULL," +
                "    is_complete INTEGER CHECK (is_complete IN (0, 1) ) " +
                "                        NOT NULL" +
                "                        DEFAULT (0) );"
        );
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS R_dungeon_quest_table (" +
                "    dungeon_id INTEGER REFERENCES dungeon_table (_id)," +
                "    quest_id   INTEGER REFERENCES quest_table (_id)," +
                "    PRIMARY KEY (" +
                "        dungeon_id," +
                "        quest_id" +
                "    )" +
                ");"
        );
        sqLiteDatabase.execSQL("CREATE TABLE R_user_dungeon_table (" +
                "    user_id     INTEGER REFERENCES user_table (_id)," +
                "    dungeon_id  INTEGER REFERENCES dungeon_table (_id)," +
                "    is_complete INTEGER CHECK (is_complete IN (0, 1) ) " +
                "                        DEFAULT (0) " +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists quest_tb");
        onCreate(sqLiteDatabase);
    }
/**
    public boolean login(String phone, String password) {
        sql = "select COUNT(*) as user_count from user_table where phone = " + phone + " and password = " + password + ";";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("user_count"));
        return count == 1;
    }

    @SuppressLint("Range")
    public Integer getUserId(String phone) {
        sql = "select _id from user_table where phone = " + phone + ";";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        int user_id = cursor.getInt(cursor.getColumnIndex("_id"));
        return user_id;
    }

    public ArrayList<QuestEntity> getDailyQuest(String user_id) {
        ArrayList<QuestEntity> list = new ArrayList<QuestEntity>();
        @SuppressLint("Recycle") Cursor cursor = db.query("R_user_quest_table", null, "user_id = " + user_id, null, null, null, "is_complete ASC, quest_id ASC");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int quest_id = cursor.getInt(cursor.getColumnIndex("quest_id"));
            @SuppressLint("Range") int combo = cursor.getInt(cursor.getColumnIndex("combo"));
            @SuppressLint("Range") int is_complete = cursor.getInt(cursor.getColumnIndex("is_complete"));
            sql = "select * from quest_table where" + " _id=" + quest_id + " and type = 'daily';";
            Cursor quest_cursor = db.rawQuery(sql, null);
            if (!quest_cursor.moveToNext()) {
                continue;
            }
            @SuppressLint("Range") String title = quest_cursor.getString(quest_cursor.getColumnIndex("title"));
            @SuppressLint("Range") int coin = quest_cursor.getInt(quest_cursor.getColumnIndex("coin"));
            @SuppressLint("Range") String self_treasure = quest_cursor.getString(quest_cursor.getColumnIndex("self_treasure"));
            QuestEnum questEnum = QuestEnum.DAILY;
            boolean complete = is_complete == 1;
            list.add(new QuestEntity(quest_id, title, complete, combo, coin, self_treasure, questEnum));
        }
        return list;
    }

    public ArrayList<QuestEntity> getWeeklyQuest(String user_id) {
        ArrayList<QuestEntity> list = new ArrayList<QuestEntity>();
        @SuppressLint("Recycle") Cursor cursor = db.query("R_user_quest_table", null, "user_id = " + user_id, null, null, null, "is_complete ASC, quest_id ASC");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int quest_id = cursor.getInt(cursor.getColumnIndex("quest_id"));
            @SuppressLint("Range") int combo = cursor.getInt(cursor.getColumnIndex("combo"));
            @SuppressLint("Range") int is_complete = cursor.getInt(cursor.getColumnIndex("is_complete"));
            sql = "select * from quest_table where" + " _id=" + quest_id + " and type = 'weekly';";
            Cursor quest_cursor = db.rawQuery(sql, null);
            if (!quest_cursor.moveToNext()) {
                continue;
            }
            @SuppressLint("Range") String title = quest_cursor.getString(quest_cursor.getColumnIndex("title"));
            @SuppressLint("Range") int coin = quest_cursor.getInt(quest_cursor.getColumnIndex("coin"));
            @SuppressLint("Range") String self_treasure = quest_cursor.getString(quest_cursor.getColumnIndex("self_treasure"));
            QuestEnum questEnum = QuestEnum.WEEKLY;
            boolean complete = is_complete == 1;
            list.add(new QuestEntity(quest_id, title, complete, combo, coin, self_treasure, questEnum));
        }
        return list;
    }

    public ArrayList<QuestEntity> getAchievementQuest(String user_id) {
        ArrayList<QuestEntity> list = new ArrayList<QuestEntity>();
        @SuppressLint("Recycle") Cursor cursor = db.query("R_user_quest_table", null, "user_id = " + user_id + " and is_complete = 0", null, null, null, "is_complete ASC, quest_id ASC");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int quest_id = cursor.getInt(cursor.getColumnIndex("quest_id"));
            @SuppressLint("Range") int combo = cursor.getInt(cursor.getColumnIndex("combo"));
            @SuppressLint("Range") int is_complete = cursor.getInt(cursor.getColumnIndex("is_complete"));
            sql = "select * from quest_table where" + " _id=" + quest_id + " and type = 'achievement';";
            Cursor quest_cursor = db.rawQuery(sql, null);
            if (!quest_cursor.moveToNext()) {
                continue;
            }
            @SuppressLint("Range") String title = quest_cursor.getString(quest_cursor.getColumnIndex("title"));
            @SuppressLint("Range") int coin = quest_cursor.getInt(quest_cursor.getColumnIndex("coin"));
            @SuppressLint("Range") String self_treasure = quest_cursor.getString(quest_cursor.getColumnIndex("self_treasure"));
            QuestEnum questEnum = QuestEnum.ACHIEVEMENT;
            boolean complete = is_complete == 1;
            list.add(new QuestEntity(quest_id, title, complete, combo, coin, self_treasure, questEnum));
        }
        return list;
    }

    public ArrayList<QuestEntity> getDungeonQuest(String user_id) {
        QuestEnum questEnum = QuestEnum.DAILY;
        ArrayList<QuestEntity> list = new ArrayList<QuestEntity>();
        @SuppressLint("Recycle") Cursor cursor = db.query("R_user_dungeon_table", null, "user_id = " + user_id + " and is_complete = 0", null, null, null, "is_complete ASC, dungeon_id ASC");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int dungeon_id = cursor.getInt(cursor.getColumnIndex("dungeon_id"));
            @SuppressLint("Recycle") Cursor dungeon_cursor = db.query("R_dungeon_quest_table", null, "dungeon_id = " + dungeon_id, null, null, null, "quest_id ASC");
            while (dungeon_cursor.moveToNext()) {
                @SuppressLint("Range") int quest_id = dungeon_cursor.getInt(dungeon_cursor.getColumnIndex("quest_id"));
                sql = "select * from quest_table where" + " _id=" + quest_id + ";";
                Cursor quest_cursor = db.rawQuery(sql, null);
                if (!quest_cursor.moveToNext()) {
                    continue;
                }
                @SuppressLint("Range") String title = quest_cursor.getString(quest_cursor.getColumnIndex("title"));
                @SuppressLint("Range") int coin = quest_cursor.getInt(quest_cursor.getColumnIndex("coin"));
                @SuppressLint("Range") String self_treasure = quest_cursor.getString(quest_cursor.getColumnIndex("self_treasure"));
                @SuppressLint("Range") String type = quest_cursor.getString(quest_cursor.getColumnIndex("type"));
                sql = "select * from R_user_quest_table where user_id = " + user_id + " and quest_id = " + quest_id + " ;";
                Cursor q_u_cursor = db.rawQuery(sql, null);
                q_u_cursor.moveToNext();
                @SuppressLint("Range") int combo = q_u_cursor.getInt(q_u_cursor.getColumnIndex("combo"));
                @SuppressLint("Range") int is_complete = q_u_cursor.getInt(q_u_cursor.getColumnIndex("is_complete"));
                switch (type) {
                    case "daily":
                        questEnum = QuestEnum.DAILY;
                        break;
                    case "weekly":
                        questEnum = QuestEnum.WEEKLY;
                        break;
                    case "achievement":
                        questEnum = QuestEnum.ACHIEVEMENT;
                        break;
                }
                boolean complete = is_complete == 1;
                list.add(new QuestEntity(quest_id, title, complete, combo, coin, self_treasure, questEnum));
            }

        }
        return list;
    }

    // 注册部分
    // 通过手机号检查是否存在该用户
    public boolean checkPerson(String phone) {
        sql = "select COUNT(*) as user_count from user_table where phone = " + phone + ";";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("user_count"));
        return count == 1;
    }

    // 看目前数据库有多少用户,得到数目用于插入数据库时的id
    public int checkPersonCount() {
        sql = "select COUNT(*) as user_count from user_table";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("user_count"));
        return count;
    }

    // 向数据库中插入用户数据
    public void register(int id, String phone, String password, String nickname, String profile, int coin) {
        //插入数据SQL语句
        String sql = "INSERT INTO user_table(_id,phone,password,nickname,profile,coin) " +
                "VALUES(" + id + ",'" + phone + "','" + password + "','" + nickname + "','" + profile + "'," + coin + ")";
        //执行SQL语句
        db.execSQL(sql);
    }

    // 忘记密码部分
    //修改已存在用户密码
    public void forget(String phone, String password) {
        //修改数据SQL语句
        String sql = "UPDATE user_table SET password = " + password +
                " WHERE phone = " + phone + ";";
        //执行SQL语句
        db.execSQL(sql);
    }

    public void complete_quest(int quest_id, int user_id) {
        sql = "UPDATE R_user_quest_table SET is_complete = 1 , combo = combo + 1 where user_id = " + user_id + " and quest_id = " + quest_id + ";";
        db.execSQL(sql);
        sql = "select coin from quest_table where _id = " + quest_id + ";";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") int coin = cursor.getInt(cursor.getColumnIndex("coin"));

        sql = "UPDATE user_table SET coin = coin + " + coin + " WHERE _id = " + user_id + ";";
        db.execSQL(sql);
    }

    public QuestEnum getQuestType(int quest_id) {
        sql = "select * from quest_table where _id = " + quest_id + ";";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
        QuestEnum questEnum = QuestEnum.DAILY;
        switch (type) {
            case "daily":
                questEnum = QuestEnum.DAILY;
                break;
            case "weekly":
                questEnum = QuestEnum.WEEKLY;
                break;
            case "achievement":
                questEnum = QuestEnum.ACHIEVEMENT;
                break;
        }
        return questEnum;
    }

    // 制定任务部分
    //插入数据SQL语句
    public void DevelopQuest(int id, String title, int coin, String self_treasure, String type) {
        String sql = "INSERT INTO quest_table(_id,title,coin,self_treasure,type) " +
                "VALUES(" + id + ",'" + title + "','" + coin + "','" + self_treasure + "','" + type + "')";
        //执行SQL语句
        db.execSQL(sql);
    }

    // 通过任务描述检查是否存在该任务
    public boolean checkQuest(String Quest, String type, int userid) {
        sql = "select COUNT(*) as quest_count from quest_table ,R_user_quest_table where title = '" + Quest + "' and type = '" + type + "' and user_id = " + userid + " and quest_table._id = R_user_quest_table.quest_id;";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("quest_count"));
        return count == 1;
    }

    public int checkQuestCount() {
        sql = "select COUNT(*) as quest_count from quest_table";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") int count = cursor.getInt(cursor.getColumnIndex("quest_count"));
        return count;
    }

    public void DevelopUserQuest(int id, int quest_id, int combo, int is_complete) {
        String sql = "INSERT INTO R_user_quest_table(user_id,quest_id,combo,is_complete) " +
                "VALUES(" + id + "," + quest_id + "," + combo + "," + is_complete + ")";
        //执行SQL语句
        db.execSQL(sql);
    }

    // 背包部分
    // 背包title信息部分
    // 通过id查询个人信息
    public PersonEntity getPersonInfo(String _id) {
        PersonEntity personEntity = new PersonEntity();
        int id = Integer.parseInt(_id);
        @SuppressLint("Recycle") Cursor cursor = db.query("user_table", null, "_id=?", new String[]{_id}, null, null, "_id DESC");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            @SuppressLint("Range") String profile = cursor.getString(cursor.getColumnIndex("profile"));
            @SuppressLint("Range") int coin = cursor.getInt(cursor.getColumnIndex("coin"));
            personEntity = new PersonEntity(id, phone, password, nickname, profile, coin);
        }
        return personEntity;
    }

    // 通过id查询成就数目
    public int getPersonAchievementNum(String _id) {
        int id = Integer.parseInt(_id);
        int count = 0;
        String sql2;
        String type = "achievement";
//        sql2 = "select _id as quest_id from quest_table where type = " + type + ";";
        Cursor cursor = db.query("quest_table", null, "type=?", new String[]{type}, null, null, "_id DESC");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int quest_id = cursor.getInt(cursor.getColumnIndex("_id"));
            sql = "select COUNT(*) as achievement_count from R_user_quest_table where user_id = " + id + " and is_complete = 1 " +
                    "and quest_id = " + quest_id + ";";
            Cursor cursor2 = db.rawQuery(sql, null);
            cursor2.moveToNext();
            @SuppressLint("Range") int count_achievement = cursor2.getInt(cursor2.getColumnIndex("achievement_count"));
            count += count_achievement;
        }
        return count;
    }

    // 通过id查其头像文件（实际上固定头像，按照数字分配头像）
    public String getPersonProfile(String _id) {
        int id = Integer.parseInt(_id);
        sql = "select profile from user_table where _id = " + _id + ";";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToNext();
        @SuppressLint("Range") String profile = cursor.getString(cursor.getColumnIndex("profile"));
        return profile;
    }

    // 背包“勇者信息”部分
    // 更新信息（不包括密码）
    public void updateUser(String _id, String profile, String nickname) {
        int id = Integer.parseInt(_id);
        ContentValues values = new ContentValues();
        values.put("nickname", nickname);
        values.put("profile", profile);
        db.update("user_table", values, "_id=?", new String[]{_id});

    }

    public ArrayList<PersonEntity> getFriends(String user_id) {
        ArrayList<PersonEntity> list = new ArrayList<PersonEntity>();
        @SuppressLint("Recycle") Cursor cursor = db.query("R_friends_table", null, "user_id = " + user_id, null, null, null, "friend_id ASC");
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int friend_id = cursor.getInt(cursor.getColumnIndex("friend_id"));
            sql = "select * from user_table where" + " _id=" + friend_id + ";";
            Cursor friend_cursor = db.rawQuery(sql, null);
            if (friend_cursor.moveToNext()) {
                @SuppressLint("Range") String nickname = friend_cursor.getString(friend_cursor.getColumnIndex("nickname"));
                @SuppressLint("Range") String profile = friend_cursor.getString(friend_cursor.getColumnIndex("profile"));
                @SuppressLint("Range") int coin = friend_cursor.getInt(friend_cursor.getColumnIndex("coin"));
                list.add(new PersonEntity(friend_id, nickname, profile, coin));
            }
        }
        return list;
    }

    public ArrayList<PersonEntity> getThisFriend(String user_id) {
        ArrayList<PersonEntity> list = new ArrayList<PersonEntity>();
        @SuppressLint("Recycle") Cursor cursor = db.query("user_table", null, "user_id = " + user_id, null, null, null, "friend_id ASC");
        if (cursor.moveToNext()) {
            @SuppressLint("Range") int friend_id = cursor.getInt(cursor.getColumnIndex("_id"));
            @SuppressLint("Range") String nickname = cursor.getString(cursor.getColumnIndex("nickname"));
            @SuppressLint("Range") String profile = cursor.getString(cursor.getColumnIndex("profile"));
            @SuppressLint("Range") int coin = cursor.getInt(cursor.getColumnIndex("coin"));
            list.add(new PersonEntity(friend_id, nickname, profile, coin));
        }

        return list;
    }

    public void updateAfterDay() throws ParseException {
        String now_date = DateUtil.getNowDate();
        sql = "select * from alarm_update_date ;";
        Cursor date_cursor = db.rawQuery(sql, null);
        if (!date_cursor.moveToNext()) {
            sql = "INSERT INTO alarm_update_date VAlUES('" + now_date + "') ;";
            db.execSQL(sql);
            sql = "select * from alarm_update_date ;";
            date_cursor = db.rawQuery(sql, null);
        }
        @SuppressLint("Range") String last_date = date_cursor.getString(date_cursor.getColumnIndex("update_date"));
        if (!Objects.equals(last_date, now_date)) {
            //未完成的每日任务连击数归零
            sql = "update R_user_quest_table SET combo = 0 " +
                    "where is_complete = 0 AND quest_id IN" +
                    "(SELECT _id From quest_table where type = 'daily')";
            db.execSQL(sql);
            sql = "update R_user_quest_table SET is_complete = 0 " +
                    "where is_complete = 1 AND quest_id IN" +
                    "(SELECT _id From quest_table where type = 'daily')";
            db.execSQL(sql);
            //清空每周连击数。
            if (DateUtil.getDayDifference(last_date, now_date) > 7 || DateUtil.getWeekOfDateNum(last_date) > DateUtil.getWeekOfDateNum(now_date)) {
                sql = "update R_user_quest_table SET combo = 0 " +
                        "where is_complete = 0 AND quest_id IN" +
                        "(SELECT _id From quest_table where type = 'weekly')";
                db.execSQL(sql);
                sql = "update R_user_quest_table SET is_complete = 0 " +
                        "where is_complete = 1 AND quest_id IN" +
                        "(SELECT _id From quest_table where type = 'weekly')";
                db.execSQL(sql);

            }
            //清空完成之后，说明已经更新完成，更新日期
            sql = "UPDATE alarm_update_date SET update_date = '" + now_date + "' WHERE update_date = '" + last_date + "' ;";
            db.execSQL(sql);
        }
    }

    public List<DungeonEntity> getDungeon(String user_id) {
        QuestEnum questEnum = QuestEnum.DAILY;
        ArrayList<DungeonEntity> list = new ArrayList<>();
        ArrayList<QuestEntity> questList = new ArrayList<>();
        boolean dun_complete;
        @SuppressLint("Recycle") Cursor dungeon_cursor = db.query("dungeon_table", null, null, null, null, null, "_id ASC");
        while (dungeon_cursor.moveToNext()) {
            @SuppressLint("Range") int dungeon_id = dungeon_cursor.getInt(dungeon_cursor.getColumnIndex("_id"));
            @SuppressLint("Range") String dungeon_title = dungeon_cursor.getString(dungeon_cursor.getColumnIndex("title"));
            @SuppressLint("Range") int dungeon_coin = dungeon_cursor.getInt(dungeon_cursor.getColumnIndex("coin"));
            @SuppressLint("Recycle") Cursor d_q_cursor = db.query("R_dungeon_quest_table", null, "dungeon_id = " + dungeon_id, null, null, null, "quest_id ASC");
            while (d_q_cursor.moveToNext()) {
                @SuppressLint("Range") int quest_id = d_q_cursor.getInt(d_q_cursor.getColumnIndex("quest_id"));
                sql = "select * from quest_table where" + " _id=" + quest_id + ";";
                Cursor quest_cursor = db.rawQuery(sql, null);
                if (!quest_cursor.moveToNext()) {
                    continue;
                }
                @SuppressLint("Range") String title = quest_cursor.getString(quest_cursor.getColumnIndex("title"));
                @SuppressLint("Range") int coin = quest_cursor.getInt(quest_cursor.getColumnIndex("coin"));
                @SuppressLint("Range") String self_treasure = quest_cursor.getString(quest_cursor.getColumnIndex("self_treasure"));
                @SuppressLint("Range") String type = quest_cursor.getString(quest_cursor.getColumnIndex("type"));
                switch (type) {
                    case "daily":
                        questEnum = QuestEnum.DAILY;
                        break;
                    case "weekly":
                        questEnum = QuestEnum.WEEKLY;
                        break;
                    case "achievement":
                        questEnum = QuestEnum.ACHIEVEMENT;
                        break;
                }
                questList.add(new QuestEntity(quest_id, title, false, 0, coin, self_treasure, questEnum));
            }
            sql = "select * from R_user_dungeon_table where" + " dungeon_id=" + dungeon_id + " and user_id = " + user_id + ";";
            Cursor u_d_cursor = db.rawQuery(sql, null);
            if (!u_d_cursor.moveToNext()) {
                list.add(new DungeonEntity(dungeon_title, false, dungeon_coin, questList));
                continue;
            }
            @SuppressLint("Range") int dun_is_complete = u_d_cursor.getInt(u_d_cursor.getColumnIndex("is_complete"));
            dun_complete = dun_is_complete == 1;
            list.add(new DungeonEntity(dungeon_title, dun_complete, dungeon_coin, questList));
        }
        return list;
    }

    ;
        */

}
