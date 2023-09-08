package com.example.questApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class VerifyLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_login_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_login);
        iv_login_change = findViewById(R.id.iv_login_change);
        iv_login_change.setImageResource(R.drawable.login_paw);
        iv_login_change.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_change:
                Intent intent = new Intent(VerifyLoginActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}