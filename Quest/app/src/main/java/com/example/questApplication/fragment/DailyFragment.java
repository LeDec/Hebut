package com.example.questApplication.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.Adapter.QuestContainerAdapter;
import com.example.questApplication.Adapter.QuestTitleAdapter;
import com.example.questApplication.DevelopQuest;
import com.example.questApplication.Entity.QuestEntity;
import com.example.questApplication.MainActivity;
import com.example.questApplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class DailyFragment extends Fragment {
    List<String> titles = new ArrayList<>();
    int count = 0, over = 0;
    List<QuestEntity> questEntityList = new ArrayList<>();
    QuestEntity questEntity = new QuestEntity();
    String type;
    private View view;
    public RecyclerView questTitleView;
    public RecyclerView questContainerView;
    private QuestContainerAdapter questContainerAdapter;
    private GifImageView gifImageView;
    private String userId;
    private TextView tv_quest_count;
    private Button btn_quest_collect;

    private boolean flag = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_daily_fragment, container, false);
        tv_quest_count = view.findViewById(R.id.tv_quest_count);
        btn_quest_collect = view.findViewById(R.id.btn_quest_collect);
        btn_quest_collect.setOnClickListener(view -> {
            AlertDialog alertDialog2 = new AlertDialog.Builder(view.getContext())
                    .setTitle("恭喜你完成所有任务！")
                    .setMessage("感谢你勇者，你已经忙了好一阵了，去我们村子里歇一会儿吧！")
                    .setIcon(R.drawable.pixel_hero)
                    .setPositiveButton("领取奖励", (dialogInterface, i) -> {
                        questContainerAdapter = new QuestContainerAdapter(getActivity(), questEntityList, gifImageView, tv_quest_count, btn_quest_collect);
                        questContainerView.setAdapter(questContainerAdapter);
                        btn_quest_collect.setEnabled(false);
                        Thread collect = new Thread(() -> {
                            try {
                                String json = "{\n" +
                                        "  \"quest_type\": \""+ type + "\",\n" +
                                        "  \"user_id\": " + userId + "\n" +
                                        "}";
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://140.210.204.183:8081/project/r-user-quest-table/collectTotal")
                                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                        .build();
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseData);
                                int code  = jsonObject.getInt("code");
                                if(code != 0){
                                    flag = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        collect.start();
                        try {
                            collect.join();
                            if(flag){
                                Toast.makeText(getActivity(),"已经领取了~",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(view.getContext(), "恭喜你，完成了所有任务！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    })
                    .setNegativeButton("我再想想", (dialogInterface, i) -> {
                    }).create();

            alertDialog2.show();
            Button mNegativeButton = alertDialog2.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button mPositiveButton = alertDialog2.getButton(AlertDialog.BUTTON_POSITIVE);

            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) mPositiveButton.getLayoutParams();
            positiveButtonLL.weight = 1;
            mPositiveButton.setLayoutParams(positiveButtonLL);

            LinearLayout.LayoutParams mNegativeButtonLL = (LinearLayout.LayoutParams) mNegativeButton.getLayoutParams();
            mNegativeButtonLL.weight = 1;
            mNegativeButton.setLayoutParams(mNegativeButtonLL);
            GifDrawable gifDrawable;
            try {
                gifDrawable = new GifDrawable(view.getResources(), R.mipmap.box);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gifImageView.setImageDrawable(gifDrawable);
            questContainerAdapter = new QuestContainerAdapter(getActivity(), questEntityList, gifImageView, tv_quest_count, btn_quest_collect);
            questContainerView.setAdapter(questContainerAdapter);
        });
        initData();
        try {
            initRecyclerView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TextView tv_develop_quest = view.findViewById(R.id.tv_developquest);
        tv_develop_quest.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DevelopQuest.class);
            startActivity(intent);
        });
        return view;
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    private void initData() {
        titles.add("每日任务");
        titles.add("每周任务");
        titles.add("成就任务");
        titles.add("副本任务");
        SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("user_id", null);
        getQuest();
    }

    private void initRecyclerView() throws IOException {
        GifDrawable gifDrawable = new GifDrawable(view.getResources(), R.mipmap.run);
        gifImageView = view.findViewById(R.id.gif_quest);
        gifImageView.setImageDrawable(gifDrawable);
        //获取RecyclerView
        questTitleView = (RecyclerView) view.findViewById(R.id.rec_quest_title);
        questContainerView = (RecyclerView) view.findViewById(R.id.rec_quest_container);
        //创建adapter
        QuestTitleAdapter questTitleAdapter = new QuestTitleAdapter(getActivity(), titles);
        questContainerAdapter = new QuestContainerAdapter(getActivity(), questEntityList, gifImageView, tv_quest_count, btn_quest_collect);
        //给RecyclerView设置adapter
        questTitleView.setAdapter(questTitleAdapter);
        questContainerView.setAdapter(questContainerAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 1);
        questContainerView.setLayoutManager(layoutManager);
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        questTitleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        questTitleView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        questTitleAdapter.setOnItemClickListener((view, data) -> {
            questEntityList.clear();
            switch (data) {
                case "每日任务":
                    type = "daily";
                    getQuest();
                    break;
                case "每周任务":
                    type = "weekly";
                    getQuest();
                    break;
                case "成就任务":
                    type = "achievement";
                    getQuest();
                    break;
                case "副本任务":
                    type = "dungeon";
                    getQuest();
                    break;
            }
            questContainerAdapter = new QuestContainerAdapter(getActivity(), questEntityList, gifImageView, tv_quest_count, btn_quest_collect);
            questContainerView.setAdapter(questContainerAdapter);
        });
    }

    public void getQuest(){
        Thread thread;
        if(Objects.equals(type, "dungeon")){
            thread = new Thread(() -> {
                try {
                    String json = "{\n" +
                            "  \"user_id\": " + userId + "\n" +
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
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
        }
        else {
            thread = new Thread(() -> {
                try {
                    String json = "{\n" +
                            "  \"quest_type\": \"" + type + "\",\n" +
                            "  \"user_id\": \"" + userId + "\"\n" +
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
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
                    if (over == count && count != 0) {
                        btn_quest_collect.setVisibility(View.VISIBLE);
                        btn_quest_collect.setEnabled(true);
                    } else {
                        btn_quest_collect.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        thread.start();
        try {
            thread.join();
            tv_quest_count.setText("(" + over + "/" + count + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}