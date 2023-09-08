package com.example.questApplication;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.questApplication.Util.GlobalVariables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class VoiceRecorderActivity extends AppCompatActivity {

    /**
     * 录音按钮，长按开始录音，松开结束
     */
    Button mBtnRecording;
    /**
     * 播放录音文件按钮
     */
    Button mBtnPlayRecording;
    Button jump;
    private MediaRecorder mediaRecorder;
    // 以文件的形式保存
    private File recordFile;
    private TextView mTVRecordingFilepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recorder);

        checkPermission();

        mTVRecordingFilepath = (TextView) findViewById(R.id.tv_recording_filepath);
        mBtnRecording = (Button) findViewById(R.id.btn_recording);
        mBtnPlayRecording = (Button) findViewById(R.id.btn_play_recording);
        jump = (Button) findViewById(R.id.jump);
        String sdCardStr = getSDPath();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTimeStr = df.format(new Date());// new Date()为获取当前系统时间
        recordFile = new File(sdCardStr, currentTimeStr + ".amr");
        GlobalVariables.staticRecordFile = recordFile;


        mBtnRecording.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        //按住事件发生后执行代码的区域
                        startRecording();
                        Toast.makeText(VoiceRecorderActivity.this, "开始录音", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        //松开事件发生后执行代码的区域
                        stopRecording();
                        Toast.makeText(VoiceRecorderActivity.this, "结束录音", Toast.LENGTH_SHORT).show();
                        mTVRecordingFilepath.setText(recordFile.getPath());
                        break;
                    }

                    default:

                        break;
                }
                return false;
            }
        });
        mBtnPlayRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 判断文件是否存在，如果存在，就播放
                 */
                if (fileIsExists(recordFile.getPath())) {
                    try {
                        Log.d("fsk","存在");
                        FileInputStream fis = new FileInputStream(recordFile);
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(fis.getFD());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoiceRecorderActivity.this,AchievementList.class);
                startActivity(intent);
            }
        });

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

    /**
     * 获取SD卡路径
     * @return SD卡路径字符串格式
     */
    public String getSDPath() {
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
//        }
//        return sdDir.toString();
        return getFilesDir().toString();
    }

    /**
     * 开始录音
     */
    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        // 判断，若当前文件已存在，则删除
        if (recordFile.exists()) {
            recordFile.delete();
        }
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());
        try {
            // 准备好开始录音
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 结束录音，释放
     */
    private void stopRecording() {
        if (recordFile != null) {
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                //e.printStackTrace();
                mediaRecorder = null;
                mediaRecorder = new MediaRecorder();
            }
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
    /**
     * 权限申请
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK};
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, 200);
                    return;
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == 200) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 200);
                    return;
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            checkPermission();
        }
    }

}
