package com.example.questApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.questApplication.Util.ProfileAndString;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonInformationDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView person_phone_detail;
    private TextView person_coin_detail;
    private TextView person_champion_detail;
    private EditText person_account_detail;
    private CircleImageView person_profile_detail;
    private TextView person_information_change;
    private TextView person_information_changePassword;
    private ImageView iv_backPackage;
    private int profile_number;
    private String userId;
    private int id_checked;
    private String string_checked = "";
    private String profile_string = "";
    private String phone;
    private String nickname;
    private int coinNum;
    private int achievementNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information_detail);

        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("user_id", null);

        person_phone_detail = findViewById(R.id.person_phone_detail);
        person_coin_detail = findViewById(R.id.person_coin_detail);
        person_champion_detail = findViewById(R.id.person_champion_detail);
        person_account_detail = findViewById(R.id.person_account_detail);
        person_profile_detail = findViewById(R.id.person_profile_detail);
        person_information_change = findViewById(R.id.person_information_change);
        person_information_changePassword = findViewById(R.id.person_information_changePassword);
        iv_backPackage = findViewById(R.id.iv_backPackage);
        person_information_change.setOnClickListener(this);
        person_information_changePassword.setOnClickListener(this);
        person_profile_detail.setOnClickListener(this);
        iv_backPackage.setOnClickListener(this);

        initData();
    }

    private void initData() {
//        PersonEntity personEntity = sqLiteUtils.getPersonInfo(userId);
//        String phone = personEntity.getPhone();
//        String nickname = personEntity.getNickname();
//        int coinNum = personEntity.getCoin();
//        int achievementNum = sqLiteUtils.getPersonAchievementNum(userId);
//        phone = ""; //缺少，暂时用的nickname
//        nickname = "";
//        coinNum = 0;
//        achievementNum = 0;
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
                        profile_string = data.getString("profile");
                        nickname = data.getString("nickname");
                        phone = "15131993716";
                        coinNum = data.getInt("coin");
                        achievementNum = data.getInt("achievement_count");
                        profile_number = ProfileAndString.StringToDrawable(profile_string);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                person_account_detail.setText(nickname);
                                person_phone_detail.setFocusableInTouchMode(false);//不可编辑
                                person_phone_detail.setText(phone);
                                person_coin_detail.setFocusableInTouchMode(false);//不可编辑
                                person_coin_detail.setText(Integer.toString(coinNum));
                                person_champion_detail.setFocusableInTouchMode(false);//不可编辑
                                person_champion_detail.setText(Integer.toString(achievementNum));
                                person_profile_detail.setImageResource(profile_number);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PersonInformationDetailActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PersonInformationDetailActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
//        profile_number = ProfileAndString.StringToDrawable(profile_string);
//        person_account_detail.setText(nickname);
//        person_phone_detail.setFocusableInTouchMode(false);//不可编辑
//        person_phone_detail.setText(phone);
//        person_coin_detail.setFocusableInTouchMode(false);//不可编辑
//        person_coin_detail.setText(Integer.toString(coinNum));
//        person_champion_detail.setFocusableInTouchMode(false);//不可编辑
//        person_champion_detail.setText(Integer.toString(achievementNum));
//        person_profile_detail.setImageResource(profile_number);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.person_information_change:
                String nickname = person_account_detail.getText().toString();
                String profile = ProfileAndString.DrawableToString(profile_number);
                new Thread(() -> {
                    try {
                        String json = "{\n" +
                                "  \"nickname\": \"" + nickname + "\",\n" +
                                "  \"profile\": \"" + profile + "\",\n" +
                                "  \"user_id\": " + userId + "\n" +
                                "}";
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://140.210.204.183:8081/project/user-table/changeInformation")
                                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.getInt("code") == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PersonInformationDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                initData();
                break;
            case R.id.person_information_changePassword:
                break;
            case R.id.person_profile_detail:
                showDialog();
                break;
            case R.id.iv_backPackage:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @SuppressLint("RestrictedApi")
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //获取界面
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
        //将界面填充到AlertDiaLog容器并去除边框
        builder.setView(view, 0, 0, 0, 0);
        //初始化控件
        TextView but_ok = view.findViewById(R.id.but_ok);
        TextView but_return = view.findViewById(R.id.but_return);
        // 头像部分
        ImageView icon_cat = view.findViewById(R.id.icon_cat);
        ImageView icon_bee = view.findViewById(R.id.icon_bee);
        ;
        ImageView icon_duck = view.findViewById(R.id.icon_duck);
        ;
        ImageView icon_food = view.findViewById(R.id.icon_food);
        ;
        ImageView icon_lattice = view.findViewById(R.id.icon_lattice);
        ImageView icon_rabbit = view.findViewById(R.id.icon_rabbit);
        //取消点击外部消失弹窗
        builder.setCancelable(false);
        //创建AlertDiaLog
        builder.create();
        //AlertDiaLog显示
        final AlertDialog dialog = builder.show();
        // 移除dialog的decorview背景色
        dialog.getWindow().getDecorView().setBackground(null);
        icon_cat.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                id_checked = R.drawable.icon_cat;
                icon_cat.setBackgroundColor(R.color.unknown_yellow);
                icon_bee.setBackgroundColor(R.color.white);
//                switch (string_checked) {
//                    case "":break;
//                    case "icon_cat":break;case "icon_bee": icon_bee.setBackgroundColor(R.color.white); break;
//                    case "icon_duck": icon_duck.setBackgroundColor(R.color.white); break;case "icon_food": icon_food.setBackgroundColor(R.color.white); break;
//                    case "icon_lattice": icon_lattice.setBackgroundColor(R.color.white); break;case "icon_rabbit": icon_rabbit.setBackgroundColor(R.color.white); break;
//                }
//                string_checked = "icon_cat";
            }
        });
        icon_bee.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                id_checked = R.drawable.icon_bee;
                icon_bee.setBackgroundColor(R.color.unknown_yellow);
            }
        });
        icon_duck.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                id_checked = R.drawable.icon_duck;
                icon_duck.setBackgroundColor(R.color.unknown_yellow);
            }
        });
        icon_food.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                id_checked = R.drawable.icon_food;
                icon_food.setBackgroundColor(R.color.unknown_yellow);
            }
        });
        icon_lattice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                id_checked = R.drawable.icon_lattice;
                icon_lattice.setBackgroundColor(R.color.unknown_yellow);
            }
        });
        icon_rabbit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                id_checked = R.drawable.icon_rabbit;
                icon_rabbit.setBackgroundColor(R.color.unknown_yellow);
            }
        });
        //设置自定义界面的点击事件逻辑
        but_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_number = id_checked;
                person_profile_detail.setImageResource(profile_number);
                dialog.dismiss();
            }
        });
        but_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}