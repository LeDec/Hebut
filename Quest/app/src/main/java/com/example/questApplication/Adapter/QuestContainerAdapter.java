package com.example.questApplication.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.DevelopQuest;
import com.example.questApplication.Entity.QuestEntity;
import com.example.questApplication.Enum.QuestEnum;
import com.example.questApplication.R;
import com.example.questApplication.VoiceRecorderActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class QuestContainerAdapter extends RecyclerView.Adapter<QuestContainerAdapter.questHolder> {
    private Context context;
    private List<QuestEntity> questEntityList;

    private GifImageView gifImageView;

    private TextView tv_count;

    private Button btn_collect;
    int count;
    int over;
    QuestEntity questEntity;
    int user_id;
    int quest_id;
    String type;



    //创建构造函数
    public QuestContainerAdapter(Context context, List<QuestEntity> questEntityList, GifImageView gifImageView, TextView tv_count, Button btn_collect) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.questEntityList = questEntityList;//实体类数据ArrayList
        this.gifImageView = gifImageView;
        this.tv_count = tv_count;
        this.btn_collect = btn_collect;
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public questHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_quest, parent, false);
        questHolder quest = new questHolder(itemView);
        quest.tv_quest_yes.setOnClickListener(new View.OnClickListener() {
            private Button mPositiveButton;
            private Button mNegativeButton;

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                TextView tv_quest_id = itemView.findViewById(R.id.tv_quest_id);
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("确定完成任务了吗？");
                    builder.setMessage("感谢你勇者，世界又和平了一分。不过还有好多事需要解决，希望你继续努力！");
                    builder.setIcon(R.drawable.pixel_hero);
                    builder.setPositiveButton("领取奖励", (dialogInterface, i) -> {
                        SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                        user_id = Integer.valueOf(sp.getString("user_id", null));
                        String quest_string = tv_quest_id.getText().toString();
                        quest_id = Integer.valueOf(quest_string.substring(4, quest_string.length()));
                        Thread complete = new Thread(() -> {
                            try {
                                String json = "{\n" +
                                        "  \"quest_id\": " + quest_id + ",\n" +
                                        "  \"user_id\": " + user_id + "\n" +
                                        "}";
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://140.210.204.183:8081/project/quest-table/completeQuest")
                                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                        .build();
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONObject data = jsonObject.getJSONObject("data");
                                if(data.getBoolean("_achievement")){
                                    ((Activity) context).runOnUiThread(() -> {
                                        AlertDialog.Builder acBuild = new AlertDialog.Builder(context);
                                        acBuild.setTitle("恭喜您完成了一项成就！");
                                        acBuild.setMessage("是否前往成就录音？");
                                        acBuild.setIcon(R.drawable.pixel_hero);
                                        acBuild.setPositiveButton("前往录音", (acDialogInterface, j) -> {
                                            Intent intent = new Intent(context, VoiceRecorderActivity.class);
                                            context.startActivity(intent);
                                        });
                                        acBuild.setNegativeButton("稍后再去", (acDialogInterface, j) -> {
                                        });
                                        AlertDialog alertDialog2 = acBuild
                                                .create();
                                        alertDialog2.show();
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        Thread check = new Thread(() -> {
                            try {
                                String json = "{\n" +
                                        "  \"quest_id\": " + quest_id + ",\n" +
                                        "  \"user_id\": " + user_id + "\n" +
                                        "}";
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://140.210.204.183:8081/project/quest-table/checkQuestInformation")
                                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                        .build();
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseData);
                                JSONObject data = jsonObject.getJSONObject("data");
                                type = data.getString("questEnum");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        if (!Objects.equals(type, QuestEnum.DUNGEON.getMsg())) {
                            Thread thread = new Thread(() -> {
                                try {
                                    String json = "{\n" +
                                            "  \"quest_type\": \"" + type + "\",\n" +
                                            "  \"user_id\": \"" + user_id + "\"\n" +
                                            "}";
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url("http://140.210.204.183:8081/project/quest-table/getQuest")
                                            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String responseData = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    count = dataObject.getInt("count");
                                    over = dataObject.getInt("over");
                                    JSONArray jsonArray = dataObject.getJSONArray("questResponseList");
                                    int cnt;
                                    questEntityList.clear();
                                    for (cnt = 0; cnt < jsonArray.length(); cnt++) {
                                        JSONObject object = jsonArray.getJSONObject(cnt);
                                        questEntity = new QuestEntity();
                                        questEntity.setQuest_id(object.getInt("quest_id"));
                                        questEntity.setQuestTitle(object.getString("title"));
                                        questEntity.setQuestType(object.getString("questEnum"));
                                        questEntity.setCoin(object.getInt("coin"));
                                        questEntity.setSelfTreasure(object.getString("self_treasure"));
                                        questEntity.setCombo(object.getInt("combo"));
                                        questEntity.setComplete(object.getInt("is_complete") == 1);
                                        questEntityList.add(questEntity);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            try {
                                complete.start();
                                complete.join();
                                check.start();
                                check.join();
                                thread.start();
                                thread.join();
                                notifyDataSetChanged();
                                tv_count.setText("(" + over + "/" + count + ")");
                                if (count == over) btn_collect.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Thread thread = new Thread(() -> {
                                try {
                                    String json = "{\n" +
                                            "  \"user_id\": " + user_id + "\n" +
                                            "}";
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url("http://140.210.204.183:8081/project/quest-table/getAppliedDungeonQuest")
                                            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                            .build();
                                    Response response = client.newCall(request).execute();
                                    String responseData = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    count = dataObject.getInt("count");
                                    over = dataObject.getInt("over");
                                    JSONArray jsonArray = dataObject.getJSONArray("questResponseList");
                                    int cnt;
                                    for (cnt = 0; cnt < jsonArray.length(); cnt++) {
                                        JSONObject object = jsonArray.getJSONObject(cnt);
                                        questEntity = new QuestEntity();
                                        questEntity.setQuest_id(object.getInt("quest_id"));
                                        questEntity.setQuestTitle(object.getString("title"));
                                        questEntity.setQuestType(object.getString("questEnum"));
                                        questEntity.setCoin(object.getInt("coin"));
                                        questEntity.setSelfTreasure(object.getString("self_treasure"));
                                        questEntity.setCombo(object.getInt("combo"));
                                        questEntity.setComplete(object.getInt("is_complete") == 1);
                                        questEntityList.add(questEntity);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            });
                            try {
                                complete.start();
                                complete.join();
                                check.start();
                                check.join();
                                thread.start();
                                thread.join();
                                tv_count.setText("(" + over + "/" + count + ")");
                                if (count == over) btn_collect.setVisibility(View.VISIBLE);
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setNegativeButton("我再想想", (dialogInterface, i) -> {
                    });
                    AlertDialog alertDialog2 = builder
                            .create();
                    alertDialog2.show();
                    mNegativeButton = alertDialog2.getButton(AlertDialog.BUTTON_NEGATIVE);
                    mPositiveButton = alertDialog2.getButton(AlertDialog.BUTTON_POSITIVE);

                    LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) mPositiveButton.getLayoutParams();
                    positiveButtonLL.weight = 1;
                    mPositiveButton.setLayoutParams(positiveButtonLL);

                    LinearLayout.LayoutParams mNegativeButtonLL = (LinearLayout.LayoutParams) mNegativeButton.getLayoutParams();
                    mNegativeButtonLL.weight = 1;
                    mNegativeButton.setLayoutParams(mNegativeButtonLL);

                    GifDrawable gifDrawable = new GifDrawable(itemView.getResources(), R.mipmap.battle);
                    gifImageView.setImageDrawable(gifDrawable);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return quest;
    }


    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(questHolder holder, int position) {
        QuestEntity data = questEntityList.get(position);
        String title = data.getQuestTitle();
        switch (data.getQuestType()) {
            case "daily":
                initDaily(holder, data, title);
                break;
            case "weekly":
                initWeekly(holder, data, title);
                break;
            case "achievement":
                initAchievement(holder, data, title);
                break;
        }
    }

    public void initDaily(questHolder holder, QuestEntity data, String title) {
        if (data.isComplete()) {
            title += "(1/1)";
            holder.layout_btn_yes.setBackgroundResource(R.drawable.button_unreachable_bg);
            holder.tv_quest_yes.setClickable(false);
        } else {
            title += "(0/1)";
        }
        holder.tv_quest_id.setText("ID: " + data.getQuest_id());
        holder.tv_quest_type.setText("每日任务");
        holder.tv_title.setText(title);
        holder.tv_combo.setText("已连续打卡 " + data.getCombo() + " 天");
        holder.tv_treasure.setText(" * " + data.getCoin());
        holder.tv_other_treasure.setText("额外奖励：" + data.getSelfTreasure());
    }

    public void initWeekly(questHolder holder, QuestEntity data, String title) {
        if (data.isComplete()) {
            title += "(1/1)";
            holder.layout_btn_yes.setBackgroundResource(R.drawable.button_unreachable_bg);
            holder.tv_quest_yes.setClickable(false);
        } else {
            title += "(0/1)";
        }
        holder.cardView.setBackgroundColor(Color.parseColor("#FF9966"));
        holder.tv_quest_id.setText("ID: " + data.getQuest_id());
        holder.tv_quest_type.setText("每周任务");
        holder.tv_title.setText(title);
        holder.tv_combo.setText("已连续打卡 " + data.getCombo() + " 周");
        holder.tv_treasure.setText(" * " + data.getCoin());
        holder.tv_other_treasure.setText("额外奖励：" + data.getSelfTreasure());
    }

    public void initAchievement(questHolder holder, QuestEntity data, String title) {
        holder.cardView.setBackgroundColor(Color.parseColor("#FF03DAC5"));
        holder.tv_quest_id.setText("ID: " + data.getQuest_id());
        holder.tv_quest_type.setText("成就任务");
        holder.tv_title.setText(title);
        holder.tv_combo.setText("人生的里程碑！");
        holder.tv_treasure.setText(" * " + data.getCoin());
        holder.tv_other_treasure.setText("额外奖励：" + data.getSelfTreasure());
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return questEntityList.size();
    }

    class questHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_btn_yes;
        CardView cardView;
        TextView tv_title;
        TextView tv_combo;
        TextView tv_treasure;
        TextView tv_quest_type;
        TextView tv_other_treasure;
        TextView tv_quest_id;
        TextView tv_quest_yes;


        public questHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_quest);
            tv_quest_type = (TextView) itemView.findViewById(R.id.tv_quest_type);
            tv_title = (TextView) itemView.findViewById(R.id.tv_quest_card_title);
            tv_combo = (TextView) itemView.findViewById(R.id.tv_quest_combo);
            tv_treasure = (TextView) itemView.findViewById(R.id.tv_quest_treasure);
            tv_other_treasure = (TextView) itemView.findViewById(R.id.other_treasure);
            tv_quest_yes = (TextView) itemView.findViewById(R.id.tv_quest_yes);
            tv_quest_id = (TextView) itemView.findViewById(R.id.tv_quest_id);
            layout_btn_yes = (LinearLayout) itemView.findViewById(R.id.layout_btn_yes);
        }
    }
}