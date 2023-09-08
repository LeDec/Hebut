package com.example.questApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.questApplication.Util.GlobalVariables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AchievementList extends AppCompatActivity implements View.OnClickListener {

    private File recordFile;
    ImageView listenVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement_list);
        recordFile = GlobalVariables.staticRecordFile;

        listenVoice = findViewById(R.id.listenVoice);
        listenVoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.listenVoice:
                /**
                 * 判断文件是否存在，如果存在，就播放
                 */
                if (fileIsExists(recordFile.getPath())) {
                    try {
                        FileInputStream fis = new FileInputStream(recordFile);
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(fis.getFD());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    /**
     * 判断文件是否存在
     * @param strFile 文件路径
     * @return 存在true，不存在false
     */
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}