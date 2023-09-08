package com.example.questApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone_register;
    private EditText et_password_register;
    private EditText et_password_register_verify;
    private EditText et_captcha_register;
    private TextView tv_login;
    private ImageView iv_back_register;
    private ImageView iv_returnLogin;

    private Button send_register_captcha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_phone_register = findViewById(R.id.et_phone_register);
        et_password_register = findViewById(R.id.et_password_register);
        et_password_register_verify = findViewById(R.id.et_password_register_verify);
        et_captcha_register = findViewById(R.id.et_captcha_register);
        iv_back_register = findViewById(R.id.iv_back_register);
        iv_returnLogin = findViewById(R.id.iv_returnLogin);
        send_register_captcha = findViewById(R.id.send_register_captcha);

        tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        iv_back_register.setOnClickListener(this);
        iv_returnLogin.setOnClickListener(this);
        send_register_captcha.setOnClickListener(this);

        et_phone_register.addTextChangedListener(new HideTextWatcher(et_phone_register, 11));

    }

    @Override
    public void onClick(View view) {
        String phoneNumber = et_phone_register.getText().toString();
        String password = et_password_register.getText().toString();
        String password_verify = et_password_register_verify.getText().toString();
        String captcha_register = et_captcha_register.getText().toString();
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_login:
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
                new Thread(() -> {
                    try {
                        String json = "{\n" +
                                "  \"password\": \"" + password + "\",\n" +
                                "  \"phone\": \"" + phoneNumber + "\"\n" +
                                "}";
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://140.210.204.183:8081/project/user-table/register")
                                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.getInt("code") == 0) {
                            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "该手机已注册", Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show());
                    }
                }).start();
                RegisterSuccess();
                break;
            case R.id.iv_back_register:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.send_register_captcha:
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
                                            Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterActivity.this, "请检查手机号是否正确!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void RegisterSuccess() {
//        int count = sqLiteUtils.checkPersonCount();
//        int id = count + 1;
//        String phone = et_phone_register.getText().toString();
//        String password = et_password_register.getText().toString();
//        String nickname = "冒险者";
//        String profile = "1";
//        int coin = 0;
//        sqLiteUtils.register(id,phone,password,nickname,profile,coin);
        Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private class HideTextWatcher implements TextWatcher {
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
                ViewUtil.hideOneInputMethod(RegisterActivity.this, mView);
            }
        }
    }
}