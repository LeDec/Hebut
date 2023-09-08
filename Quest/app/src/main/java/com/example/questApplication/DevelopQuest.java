package com.example.questApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.questApplication.Util.IPSectionFilter;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DevelopQuest extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    //写返回功能
    private ImageView iv_back;
    private TextView tv_add;
    private EditText et_questname;
    private EditText et_coin;
    private EditText selfaward;
    private CheckBox btndaily;
    private CheckBox btnweekly;
    private CheckBox btnsuccess;
    private String userId;
    private int UserId;
    private String type = null;

    private static String ip = "140.210.204.183:8081";

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.btndaily:
                if (btndaily.isChecked()) {
                    //setChecked(),更改此按钮的选中状态 如果为false,则不能选中该控件
                    type = "daily";
                    btnweekly.setChecked(false);
                    btnsuccess.setChecked(false);
                }
                break;
            case R.id.btnweekly:
                if (btnweekly.isChecked()) {
                    type = "weekly";
                    btndaily.setChecked(false);
                    btnsuccess.setChecked(false);
                }
                break;
            case R.id.btnsuccess:
                if (btnsuccess.isChecked()) {
                    type = "achievement";
                    btndaily.setChecked(false);
                    btnweekly.setChecked(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developquest);

        iv_back = (ImageView) findViewById(R.id.iv_backmain);
        iv_back.setOnClickListener(new View.OnClickListener() {
            //跳转到搜索的页面 不需要像Activity之间跳转的那样，自己定义方法名，之间onClick就可以了
            @Override
            public void onClick(View view) {
                //到这里使用网上搜到的就可以了正常实现了。
                Intent intent = new Intent(DevelopQuest.this, MainActivity.class);
                startActivity(intent);
            }

        });
        tv_add = findViewById(R.id.tv_add);
        tv_add.setOnClickListener(new View.OnClickListener() {
            //跳转到搜索的页面 不需要像Activity之间跳转的那样，自己定义方法名，之间onClick就可以了
            @Override
            public void onClick(View view) {
                String title1 = et_questname.getText().toString();
                String coin = et_coin.getText().toString();
                SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                userId = sp.getString("user_id", null);
                UserId = Integer.valueOf(userId).intValue();
                if (title1.length() == 0) {
                    Toast.makeText(DevelopQuest.this, "请输入任务名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == null) {
                    Toast.makeText(DevelopQuest.this, "请选择任务类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (coin.length() == 0) {
                    Toast.makeText(DevelopQuest.this, "请输入奖励金币个数，默认将设置金币数为0！", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (sqLiteUtils.checkQuest(et_questname.getText().toString(),type,UserId) == true) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(DevelopQuest.this);
//                    builder.setTitle("该任务已制定");
//                    builder.setMessage("该任务已制定，开启新的任务冒险吧！");
//                    builder.setPositiveButton("好的", null);
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                    return;
//                }
                // 检查任务是否已经制定过,已经制定code=0
                // url:http://192.168.1.109:8081/project/quest-table/checkQuest以及json:title、type、UserId，格式待更改
                DevelopQuestSuccess();
            }

        });
        btndaily = findViewById(R.id.btndaily);
        btndaily.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) DevelopQuest.this);

        btnweekly = findViewById(R.id.btnweekly);
        btnweekly.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) DevelopQuest.this);
        btnsuccess = findViewById(R.id.btnsuccess);
        btnsuccess.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) DevelopQuest.this);
        et_questname = findViewById(R.id.questname);
        et_coin = findViewById(R.id.coin_layout);
        //获取适配器里面的EditText id
        et_coin.setFilters(new InputFilter[]{new IPSectionFilter()});


        selfaward = findViewById(R.id.selfaward);

    }


    private void DevelopQuestSuccess() {
        String self_award = selfaward.getText().toString();
        String title = et_questname.getText().toString();
        int coin = Integer.parseInt(et_coin.getText().toString());
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("user_id", null);
        UserId = Integer.valueOf(userId).intValue();
        if (self_award.length() == 0) {
            self_award = "无";
        }
//        int count=sqLiteUtils.checkQuestCount();
//        int questid=count+1;
        //sqLiteUtils.DevelopQuest(questid,title,coin,self_award,type);
        // 制定新任务,空余_id，combo,is_complete
        // url:http://192.168.1.109:8081/project/quest_table/developQuest以及json:title,coin,self_treasure,type//，记得添加,combo,is_complete
        String finalSelf_award = self_award;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "{\n" +
                            "  \"coin\": \"" + coin + "\",\n" +
                            "  \"self_treasure\": \"" + finalSelf_award + "\",\n" +
                            "  \"title\": \"" + title + "\",\n" +
                            "  \"type\": \"" + type + "\",\n" +
                            "  \"user_id\": \"" + userId + "\"\n" +
                            "}";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://140.210.204.183:8081/project/quest-table/developQuest")
                            .post(RequestBody.create(MediaType.parse("application/json"), json))
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    if (jsonObject.getInt("code") == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DevelopQuest.this, "成功创建任务！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DevelopQuest.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else if (jsonObject.getInt("code") == 4) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DevelopQuest.this);
                                builder.setTitle("该任务已制定");
                                builder.setMessage("该任务已制定，开启新的任务冒险吧！");
                                builder.setPositiveButton("好的", null);
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DevelopQuest.this, "创建任务失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DevelopQuest.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
//        int combo=0;
//        int is_complete=0;
//        sqLiteUtils.DevelopUserQuest(UserId,questid,combo,is_complete);
    }


}
