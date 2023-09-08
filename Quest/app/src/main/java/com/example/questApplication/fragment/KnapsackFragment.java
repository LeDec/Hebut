package com.example.questApplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.questApplication.AchievementList;
import com.example.questApplication.HeroRecordActivity;
import com.example.questApplication.LoginActivity;
import com.example.questApplication.PersonInformationDetailActivity;
import com.example.questApplication.R;
import com.example.questApplication.Util.ProfileAndString;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KnapsackFragment extends Fragment {

    private ImageView package_icon;
    private RelativeLayout package_content;
    private View view;
    private TextView package_people_name;
    private TextView package_people_coin;
    private TextView package_people_champion;
    private ImageView package_people_profile;
    private ImageView package_information_detail;
    private ImageView package_achievement_detail;

    private ImageView iv_hero_record;
    private TextView quit_login;
    private String userId;
    private String nickname = "";
    private int coin;
    private int achievement_num;
    private String profile = "";
    //    private ProfileAndString profileAndString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_knapsack_fragment, null);
        package_people_name = view.findViewById(R.id.package_people_name);
        package_people_coin = view.findViewById(R.id.package_people_coin);
        package_people_champion = view.findViewById(R.id.package_people_champion);
        package_people_profile = view.findViewById(R.id.package_people_profile);
        package_information_detail = view.findViewById(R.id.package_information_detail);
        package_achievement_detail = view.findViewById(R.id.package_achievement_detail);
        iv_hero_record = view.findViewById(R.id.iv_hero_record);
        SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("user_id", null);
        initData();
        return view;
    }

    private void initData() {
//        PersonEntity personEntity = sqLiteUtils.getPersonInfo(userId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "{\n" +
                            "  \"user_id\": \" " + userId + "\"\n" +
                            "}";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://140.210.204.183:8081/project/user-table/myInformation")
                            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    if (jsonObject.getInt("code") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        coin = data.getInt("coin");
                        nickname = data.getString("nickname");
                        profile = data.getString("profile");
                        achievement_num = data.getInt("achievement_count");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setPersonIcon(profile);
                                package_people_name.setText(nickname);
                                package_people_champion.setText("×" + Integer.toString(achievement_num));
                                package_people_coin.setText("×" + Integer.toString(coin));
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "网络连接失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
//        nickname = personEntity.getNickname();
//        coin = personEntity.getCoin();
//        achievement_num = sqLiteUtils.getPersonAchievementNum(userId);  //这里还没有更改完毕
//        profile = sqLiteUtils.getPersonProfile(userId);
//        Toast.makeText(getActivity(),"nick,pro,id,coin,ach:" + nickname  + profile + userId+ coin + achievement_num,Toast.LENGTH_SHORT).show();
//        setPersonIcon(profile);
//        package_people_name.setText(nickname);
//        package_people_champion.setText("×" + Integer.toString(achievement_num));
//        package_people_coin.setText("×" + Integer.toString(coin));
    }

    // 设置头像
    private void setPersonIcon(String profile) {
        int id = ProfileAndString.StringToDrawable(profile);
        package_people_profile.setImageResource(id);
    }

    //用onActivityCreated函数实现，用getActivity()获取当前Activity
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        package_icon = (ImageView) getActivity().findViewById(R.id.package_icon);
        package_content = (RelativeLayout) getActivity().findViewById(R.id.package_content);
        quit_login = (TextView) getActivity().findViewById(R.id.quit_login);
        // 背包收起
        package_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (package_content.getVisibility() == View.VISIBLE) {
                    package_content.setVisibility(View.GONE);
                } else {
                    package_content.setVisibility(View.VISIBLE);
                }
            }
        });
        // 打开个人信息详情页面
        package_information_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonInformationDetailActivity.class);
                startActivity(intent);
            }
        });

        //打开英雄历程
        iv_hero_record.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HeroRecordActivity.class);
            startActivity(intent);
        });
        // 退出登录
        quit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        package_achievement_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AchievementList.class);
                startActivity(intent);
            }
        });
    }
}