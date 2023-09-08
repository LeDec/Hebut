package com.example.questApplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.questApplication.Entity.QuestInfoEntity;
import com.example.questApplication.Util.ChartUtil;
import com.example.questApplication.Util.PieChartUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HeroRecordActivity extends AppCompatActivity {

    List<QuestInfoEntity> questInfoEntityList = new ArrayList<>();
    String msg = "";
    private LineChart chart_seven_day_record;
    private PieChart chart_today_record;
    private PieChart chart_weekly_record;
    private TextView tv_quest_information;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_record);
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        userId = sp.getString("user_id", null);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "{\n" +
                            "  \"user_id\": " + userId +
                            "}";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://140.210.204.183:8081/project/quest-infor-table/checkQuestInfor")
                            .post(RequestBody.create(MediaType.parse("application/json"), json))
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    if (jsonObject.getInt("code") == 0) {
                        questInfoEntityList = new ArrayList<>();
                        JSONArray data = jsonObject.getJSONArray("data");
                        int cnt;
                        for (cnt = 0; cnt < data.length(); cnt++) {
                            JSONObject object = data.getJSONObject(cnt);
                            QuestInfoEntity questInfoEntity = new QuestInfoEntity(
                                    object.getInt("_id"),
                                    object.getInt("questId"),
                                    object.getString("title"),
                                    object.getString("type"),
                                    object.getString("createTime")
                            );
                            questInfoEntityList.add(questInfoEntity);
                        }
                        for (QuestInfoEntity questInfoEntity : questInfoEntityList){
                            msg += "您于 " + questInfoEntity.getCreateTime() + "打卡了 <" + questInfoEntity.getType()
                                     + ">  "+questInfoEntity.getTitle() + "\n";
                        }
                    }
                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
        try {
            thread.join();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        chart_seven_day_record = (LineChart) findViewById(R.id.chart_seven_day_record);
        chart_today_record = (PieChart) findViewById(R.id.chart_today_record);
        chart_weekly_record = (PieChart) findViewById(R.id.chart_weekly_record);
        chart_seven_day_record = (LineChart) findViewById(R.id.chart_seven_day_record);
        tv_quest_information = (TextView) findViewById(R.id.tv_quest_information);
        tv_quest_information.setText(msg);
        showRingPieChart();
        showLineChart();
    }

    private void showRingPieChart() {

        List<PieEntry> yvals = new ArrayList<>();
        yvals.add(new PieEntry(2.0f));
        yvals.add(new PieEntry(1.0f));

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#98FB98"));
        colors.add(Color.parseColor("#f1e9d2"));
        PieChartUtil pieChartUtil = new PieChartUtil(chart_today_record);
        pieChartUtil.showRingPieChart(yvals, colors);


        List<PieEntry> yvals1 = new ArrayList<>();
        yvals1.add(new PieEntry(1.0f));
        yvals1.add(new PieEntry(1.0f));

        PieChartUtil pieChartUtil1 = new PieChartUtil(chart_weekly_record);
        pieChartUtil1.showRingPieChart(yvals1, colors);

    }

    private void showLineChart() {
        ChartUtil.chart_init(chart_seven_day_record);
        startChart();//启动画图线程
    }

    private void startChart() {
        List<Entry> list = new ArrayList<>();

        for (int i = 0; i < 7; i++)
        {
            int value = (int) (Math.random() * 100);
            list.add(new Entry(i,value));
        }
        LineDataSet lineDataSet=new LineDataSet(list,"金币数");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(ContextCompat.getDrawable(this,R.drawable.line_chart_gradient));
        LineData lineData=new LineData(lineDataSet);
        chart_seven_day_record.setData(lineData);
        chart_seven_day_record.animateY(1000);
        chart_seven_day_record.getDescription().setEnabled(false);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}