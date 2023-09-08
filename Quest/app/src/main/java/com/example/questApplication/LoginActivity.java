package com.example.questApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.questApplication.Util.ViewUtil;

import org.json.JSONObject;

import java.text.ParseException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_phone;
    private EditText et_password;
    private CheckBox ckb_remember_paw;
    private TextView tv_login;
    private TextView tv_forget_paw;
    private ImageView iv_paw;
    private ImageView iv_login_change;
    private View input_layout;
    private View verify_input_layout;

    private ImageView iv_register;
    private boolean login_flag;

    private ImageView Public;
    //用于获取EditText的内容
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean VISIBLEA = false;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        input_layout = findViewById(R.id.input_layout);
        verify_input_layout = findViewById(R.id.verify_input_layout);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        ckb_remember_paw = findViewById(R.id.ckb_remember_paw);
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("phone", "");
            String password = pref.getString("password", "");
            et_phone.setText(account);
            et_password.setText(password);
            ckb_remember_paw.setChecked(true);
        }
        tv_forget_paw = findViewById(R.id.tv_forget_paw);
        tv_login = findViewById(R.id.tv_login);
        iv_paw = findViewById(R.id.iv_paw);
        iv_login_change = findViewById(R.id.iv_login_change);
        iv_login_change.setOnClickListener(this);
        //跳转注册
        iv_register = findViewById(R.id.iv_register);
        iv_register.setOnClickListener(this);
        //
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone, 11));
        tv_forget_paw.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        Public = findViewById(R.id.paw_public);
        Public.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phoneNumber = et_phone.getText().toString();
        String passwordS = et_password.getText().toString();
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_forget_paw: { // 跳到找回密码页面
                // 以下携带手机号码跳转到找回密码页面
                intent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.tv_login:
                if (phoneNumber.length() < 11) {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                Thread thread = new Thread(() -> {
                    try {
                        String json = "{\n" +
                                "  \"password\": \"" + passwordS + "\",\n" +
                                "  \"phone\": \"" + phoneNumber + "\"\n" +
                                "}";
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://140.210.204.183:8081/project/user-table/login")
                                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);
                        if (jsonObject.getInt("code") == 0) {
                            editor = pref.edit();
                            if (ckb_remember_paw.isChecked()) {
                                editor.putBoolean("remember_password", true);
                                editor.putString("phone", phoneNumber);
                                editor.putString("password", passwordS);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            JSONObject data = jsonObject.getJSONObject("data");
                            LoginSuccess(data.getString("userId"));
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_login_change:
                if (input_layout.getVisibility() == View.VISIBLE) {
                    input_layout.setVisibility(View.GONE);
                    verify_input_layout.setVisibility(View.VISIBLE);
                    iv_login_change.setImageResource(R.drawable.login_paw);

                } else {
                    input_layout.setVisibility(View.VISIBLE);
                    verify_input_layout.setVisibility(View.GONE);
                    iv_login_change.setImageResource(R.drawable.verify_code);
                }
                break;
            case R.id.iv_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.paw_public:
                if (!VISIBLEA) {
                    Public.setImageResource(R.drawable.pawpublic);
                    et_password.setInputType(InputType.
                            TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    int textLength1 = et_password.getText().length();
                    et_password.setSelection(textLength1, textLength1);
                    VISIBLEA = true;
                } else {
                    Public.setImageResource(R.drawable.pawunpublic);
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    int textLength2 = et_password.getText().length();
                    et_password.setSelection(textLength2, textLength2);
                    VISIBLEA = false;
                }
                break;
        }
    }

    private void LoginSuccess(String user_id) throws ParseException {
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit()
                .putString("user_id", user_id)
                .putString("password", et_password.getText().toString())
                .apply();
        Thread thread = new Thread(() -> {
            try {
                String json = "{\n" +
                        "  \"user_id\": " + user_id + "\n" +
                        "}";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://140.210.204.183:8081/project/r-user-quest-table/updateQuest")
                        .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                JSONObject jsonObject = new JSONObject(responseData);
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "网络连接失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        thread.start();
        try {
            thread.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
                ViewUtil.hideOneInputMethod(LoginActivity.this, mView);
            }
        }
    }
}