package com.example.questApplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.questApplication.Adapter.VillageFriendsAdapter;
import com.example.questApplication.Adapter.VillageNewsAdapter;
import com.example.questApplication.Entity.NoteEntity;
import com.example.questApplication.Entity.PersonEntity;
import com.example.questApplication.LoginActivity;
import com.example.questApplication.R;
import com.example.questApplication.Util.DateUtil;
import com.example.questApplication.Util.SQLiteUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VillageFragment extends Fragment {

    private View view;
    public RecyclerView newsRecyclerView;

    public RecyclerView friendsRecyclerView;
    private VillageNewsAdapter villageNewsAdapter;
    private VillageFriendsAdapter villageFriendsAdapter;

    private TextView tv_date;
    private TextView tv_news;


    List<PersonEntity> friendsList = new ArrayList<>();

    PersonEntity personEntity;

    NoteEntity noteEntity;
    List<NoteEntity> noteEntityList = new ArrayList<>();
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_village_fragment, container, false);
        initData();
        initRecyclerView();
        tv_news = view.findViewById(R.id.tv_news);
        //设置点击事件
        tv_news.setOnClickListener(new View.OnClickListener() {
            //跳转到搜索的页面 不需要像Activity之间跳转的那样，自己定义方法名，之间onClick就可以了
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_news:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("进入Quest,开启专属冒险!");
                        builder.setMessage("一个连续打卡10年人的建议：打卡是为了督促我们完成目标，而有趣的方式便是我们坚持下去的不竭动力，而Quest正是因此而诞生，在这里完成目标的同时享受游戏世界的快乐，成就与快乐皆可获得，快来看看吧！");
                        builder.setPositiveButton("关闭", null);
                        AlertDialog alert = builder.create();
                        alert.show();
                }
            }
        });
        return view;
    }

    private void initData() {
        SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("user_id", null);
        Thread friendThread = new  Thread(() -> {
            try {
                String json = "{\n" +
                        "  \"user_id\": " + userId + "\n" +
                        "}";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://140.210.204.183:8081/project/r-friends-table/getFriends")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                if (jsonObject.getInt("code") == 0) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray friendsResponseList = data.getJSONArray("friendsResponseList");
                    int cnt;
                    for (cnt = 0; cnt < friendsResponseList.length(); cnt++) {
                        JSONObject object = friendsResponseList.getJSONObject(cnt);
                        personEntity.set_id(object.getInt("user_id"));
                        personEntity.setNickname(object.getString("nickname"));
                        personEntity.setProfile(object.getString("profile"));
                        personEntity.setCoin(object.getInt("coin"));
                        friendsList.add(personEntity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Thread newsThread = new  Thread(() -> {
            try {
                String json = "{\n" +
                        "  \"user_id\": 1\n" +
                        "}";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://140.210.204.183:8081/project/note-table/checkNoteList")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
                if (jsonObject.getInt("code") == 0) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray newsResponseList = data.getJSONArray("noteTableList");
                    int cnt;
                    for (cnt = 0; cnt < newsResponseList.length(); cnt++) {
                        JSONObject object = newsResponseList.getJSONObject(cnt);
                        noteEntity = new NoteEntity();
                        noteEntity.set_id(object.getInt("id"));
                        noteEntity.setTitle(object.getString("title"));
                        noteEntity.setSay(object.getString("article"));
                        noteEntity.setApplicant("admin");
                        noteEntityList.add(noteEntity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        friendThread.start();
        newsThread.start();
        try {
            friendThread.join();
            newsThread.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initRecyclerView() {
        tv_date = view.findViewById(R.id.tv_date);
        String date = DateUtil.getNowDate() + " " + DateUtil.getWeekOfDate(new Date());
        tv_date.setText(date);
        friendsRecyclerView = (RecyclerView) view.findViewById(R.id.rec_friends);
        villageFriendsAdapter = new VillageFriendsAdapter(getActivity(), friendsList);
        friendsRecyclerView.setAdapter(villageFriendsAdapter);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        friendsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.rec_news);
        villageNewsAdapter = new VillageNewsAdapter(getActivity(), noteEntityList);
        newsRecyclerView.setAdapter(villageNewsAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 1);
        newsRecyclerView.setLayoutManager(layoutManager);

    }


}