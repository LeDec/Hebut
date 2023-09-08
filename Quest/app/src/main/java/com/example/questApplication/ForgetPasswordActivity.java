package com.example.questApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questApplication.Util.ViewUtil;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_phone_forget;
    private EditText new_paw;
    private EditText renew_paw;
    private EditText et_captcha_forget;
    private TextView tv_change;
    //写返回功能
    private ImageView iv_backlogin;
    private Button btn_cap;
    private ImageView Public_forget;
    private ImageView rePublic_forget;
    private boolean VISIBLEA = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        iv_backlogin = (ImageView) findViewById(R.id.iv_back);
        iv_backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        et_phone_forget = findViewById(R.id.et_phone_forget);
        new_paw = findViewById(R.id.new_paw);
        btn_cap = findViewById(R.id.et_captcha);
        renew_paw = findViewById(R.id.renew_paw);
        et_captcha_forget = findViewById(R.id.et_captcha_forget);
        tv_change = findViewById(R.id.tv_change);
        tv_change.setOnClickListener(this);
        et_phone_forget.addTextChangedListener(new ForgetPasswordActivity.HideTextWatcher(et_phone_forget, 11));
        btn_cap.setOnClickListener(this);
        Public_forget = findViewById(R.id.fpaw_public);
        Public_forget.setOnClickListener(this);
        rePublic_forget = findViewById(R.id.repaw_public);
        rePublic_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String phoneNumber = et_phone_forget.getText().toString();
        String password = new_paw.getText().toString();
        String password_verify = renew_paw.getText().toString();
        String captcha_register = et_captcha_forget.getText().toString();
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_change:
                if (phoneNumber.length() < 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(password_verify)) {
                    Toast.makeText(this, "密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                String captcha = sp.getString("verify_code", null);
                if (!captcha_register.equals(captcha)) {
                    Toast.makeText(this, "验证码有误！", Toast.LENGTH_SHORT).show();
                    return;
                }

                ChangeSuccess();
                break;
            case R.id.et_captcha:

                if (phoneNumber.length() == 11) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String json = "{\n" +
                                        "  \"phone\": \"" + phoneNumber + "\"\n" +
                                        "}";
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url("http://140.210.204.183:8081/project/message/send")
                                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                        .build();
                                Response response = client.newCall(request).execute();
                                String responseData = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseData);
                                if (jsonObject.getInt("code") == 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ForgetPasswordActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                                    sp.edit()
                                            .putString("verify_code", String.valueOf(data.getInt("verify_code")))
                                            .apply();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "请检查手机号是否正确!", Toast.LENGTH_SHORT).show();
                }

//                if (sqLiteUtils.checkPerson(phoneNumber) == false) {
//                    Toast.makeText(this, "该手机未注册！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // 检查手机号是否已经注册,已经注册返回0，才能忘记密码！！待统一
                // url:http://192.168.1.109:8081/project/user-table/register以及json:电话号码，格式待更改

                break;
            case R.id.fpaw_public:
                if (!VISIBLEA) {
                    Public_forget.setImageResource(R.drawable.pawpublic);
                    new_paw.setInputType(InputType.
                            TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //修改inputtype为可见

                    //将光标移动至最后
                    int textLength1 = new_paw.getText().length();
                    new_paw.setSelection(textLength1, textLength1);

                    //将是否可见设置为是
                    VISIBLEA = true;

                    //如果可见
                } else {
                    Public_forget.setImageResource(R.drawable.pawunpublic);//设置显示图标
                    new_paw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    //修改inputtype为不可见

                    //将光标移动至最后
                    int textLength2 = new_paw.getText().length();
                    new_paw.setSelection(textLength2, textLength2);
                    VISIBLEA = false;
                }
                break;
            case R.id.repaw_public:
                if (!VISIBLEA) {
                    rePublic_forget.setImageResource(R.drawable.pawpublic);
                    renew_paw.setInputType(InputType.
                            TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //修改inputtype为可见

                    //将光标移动至最后
                    int textLength1 = renew_paw.getText().length();
                    renew_paw.setSelection(textLength1, textLength1);

                    //将是否可见设置为是
                    VISIBLEA = true;

                    //如果可见
                } else {
                    rePublic_forget.setImageResource(R.drawable.pawunpublic);//设置显示图标
                    renew_paw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    //修改inputtype为不可见

                    //将光标移动至最后
                    int textLength2 = renew_paw.getText().length();
                    renew_paw.setSelection(textLength2, textLength2);
                    VISIBLEA = false;
                }
                break;
        }
    }

    private void ChangeSuccess() {
        String phone = et_phone_forget.getText().toString();
        String password = new_paw.getText().toString();
        // 修改密码,修改完成返回0，表示完成
        // url:http://192.168.1.109:8081/project/user-table/forget以及json:电话号码、密码，格式待更改
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String json = "{\n" +
                            "  \"new_password\": \"" + password + "\",\n" +
                            "  \"phone\": \"" + phone + "\"\n" +

                            "}";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://140.210.204.183:8081/project/user-table/forgetPassword")
                            .post(RequestBody.create(MediaType.parse("application/json"), json))
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    if (jsonObject.getInt("code") == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ForgetPasswordActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {//我这应该跑不到这里，，感觉本来不该有else
                            @Override
                            public void run() {
                                Toast.makeText(ForgetPasswordActivity.this, "该手机号未注册，请先进行注册！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgetPasswordActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

        //sqLiteUtils.forget(phone,password);这里把跳转login写在if那里了

    }

    class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;

        public HideTextWatcher(EditText v, int maxLength) {
            this.mView = v;
            this.mMaxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == mMaxLength) {
                ViewUtil.hideOneInputMethod(ForgetPasswordActivity.this, mView);
            }
        }
    }
}