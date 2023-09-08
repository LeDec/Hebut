package com.example.questApplication.Util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

public class ChartUtil {

    public static void chart_init(LineChart chart)
    {
        //chart.setOnChartValueSelectedListener(this);
        // enable description text
        chart.getDescription().setEnabled(true);
        // enable touch gestures
        chart.setTouchEnabled(true);

        /****************设置描述信息*************/
        chart.setNoDataText("您已经七天没有金币入账了捏QAQ");//没有数据时显示的文字
        chart.setNoDataTextColor(Color.BLACK);//没有数据时显示文字的颜色
        chart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        chart.setDrawBorders(false);//绘制图表边框的线
        // enable scaling and dragging
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        //chart.setBackgroundColor(0x4169E1FF);

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        //x轴配置
        XAxis xl = chart.getXAxis();
        //xl.setTypeface(tfLight);
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);//X轴文字显示位置
        xl.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "5-" + (int)value;
            }
        });
        //左y轴配置
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        //右y轴配置
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }


    private static LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.RED);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(9f);
        set.setDrawValues(true);
        return set;
    }


}
