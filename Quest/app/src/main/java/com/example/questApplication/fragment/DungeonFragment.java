package com.example.questApplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.Adapter.DungeonContainerAdapter;
import com.example.questApplication.Adapter.DungeonTitleAdapter;
import com.example.questApplication.Adapter.QuestTitleAdapter;
import com.example.questApplication.Entity.DungeonEntity;
import com.example.questApplication.Entity.DungeonQuestEntity;
import com.example.questApplication.Entity.QuestEntity;
import com.example.questApplication.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DungeonFragment extends Fragment {

    private View view;
    public RecyclerView cardRecyclerView;

    public RecyclerView titleRecyclerView;
    private DungeonTitleAdapter dungeonTitleAdapter;
    private DungeonContainerAdapter dungeonContainerAdapter;

    List<String> titles = new ArrayList<>();
    List<DungeonQuestEntity> questEntityList = new ArrayList<>();
    List<DungeonEntity> dungeonEntityList = new ArrayList<>();
    DungeonEntity dungeonEntity;

    DungeonQuestEntity questEntity;

    int itemPosition = 0;
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dungeon_fragment, container, false);
        initData();
        initRecyclerView();
        return view;
    }

    private void initData() {
        titles.add("世界の副本");
        titles.add("待开发の副本");
        SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("user_id", null);
        new Thread(() -> {
            try {
                String json = "{\n" +
                        "  \"user_id\": " + userId + "\n" +
                        "}";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://140.210.204.183:8081/project/r-user-dungeon-table/getApplyDungeon")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray dungeonList = data.getJSONArray("dungeonResponseList");
                for (int i = 0; i < dungeonList.length(); i++) {
                    JSONObject dungeon = dungeonList.getJSONObject(i);
                    dungeonEntity = new DungeonEntity();
                    dungeonEntity.setDungeon_id(dungeon.getInt("dungeon_id"));
                    dungeonEntity.setDungeonTitle(dungeon.getString("title"));
                    dungeonEntity.setCoin(dungeon.getInt("coin"));
                    dungeonEntity.setComplete(dungeon.getInt("is_complete") == 1);
                    JSONArray questList = dungeon.getJSONArray("questList");
                    questEntityList = new ArrayList<>();
                    for(int j = 0 ; j < questList.length() ; j++){
                        JSONObject quest = questList.getJSONObject(j);
                        questEntity = new DungeonQuestEntity();
                        questEntity.setQuest_id(quest.getInt("quest_id"));
                        questEntity.setQuest_type(quest.getString("questEnum"));
                        questEntity.setQuest_title(quest.getString("title"));
                        questEntityList.add(questEntity);
                    }
                    dungeonEntity.setQuestEntityList(questEntityList);
                    dungeonEntityList.add(dungeonEntity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void initRecyclerView() {
        cardRecyclerView = (RecyclerView) view.findViewById(R.id.rec_dungeon_card);
        titleRecyclerView = (RecyclerView) view.findViewById(R.id.rec_dungeon_title);
        dungeonContainerAdapter = new DungeonContainerAdapter(getActivity(), dungeonEntityList);
        dungeonTitleAdapter = new DungeonTitleAdapter(getActivity(), titles);
        cardRecyclerView.setAdapter(dungeonContainerAdapter);
        titleRecyclerView.setAdapter(dungeonTitleAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 1);
        cardRecyclerView.setLayoutManager(layoutManager);
        titleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        titleRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        dungeonTitleAdapter.setOnItemClickListener(new QuestTitleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, String data) {
                dungeonEntityList.clear();
                //此处进行监听事件的业务处理
                switch (data) {
                    case "世界の副本":
                        itemPosition = 0;
                        break;
                    case "待开发の副本":
                        itemPosition = 1;
                        break;
                }
            }
        });
        dungeonContainerAdapter = new DungeonContainerAdapter(getActivity(), dungeonEntityList);
    }
}