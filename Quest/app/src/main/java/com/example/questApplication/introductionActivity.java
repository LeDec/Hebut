package com.example.questApplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class introductionActivity extends AppCompatActivity {
    ImageView img, img_logo;
    TextView text_appname;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        final Intent it = new Intent(this, LoginActivity.class); //你要转向的Activity
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(it); //执行
            }
        };
        timer.schedule(task, 4200); //4.3秒后

        img = findViewById(R.id.img);
        img_logo = findViewById(R.id.img_logo);
        text_appname = findViewById(R.id.text_appname);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);

//        img.setBackgroundColor(android.graphics.Color.parseColor("#FFE76F"));

        img.animate().translationY(-2200).setDuration(2000).setStartDelay(3000);
        img_logo.animate().translationY(-2200).setDuration(2000).setStartDelay(3000);
        text_appname.animate().translationY(-2200).setDuration(2000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(-2200).setDuration(2000).setStartDelay(3000);
    }
}