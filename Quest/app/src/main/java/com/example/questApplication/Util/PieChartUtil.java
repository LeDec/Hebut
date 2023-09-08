package com.example.questApplication.Util;

import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.List;

public class PieChartUtil {

    public PieChart pieChart;

    public PieChartUtil(PieChart pieChart) {
        this.pieChart = pieChart;
        initPieChart();
    }

    //初始化
    private void initPieChart() {
        // 显示百分比
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        // 设置偏移量
        pieChart.setExtraOffsets(5, 10, 5, 5);
        // 设置滑动减速摩擦系数
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterText("完成情况");

        pieChart.setDrawHoleEnabled(true);

        pieChart.setHoleColor(Color.TRANSPARENT);
        // 设置环形图和中间空心圆之间的圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        // 设置环形图和中间空心圆之间的圆环的透明度
        pieChart.setTransparentCircleAlpha(110);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        // 设置piecahrt图表点击Item高亮是否可用
        pieChart.setHighlightPerTapEnabled(true);
        // 设置pieChart图表展示动画效果，动画运行1.4秒结束
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        //设置pieChart是否只显示饼图上百分比不显示文字
        pieChart.setDrawEntryLabels(true);

        // 设置圆孔半径
        pieChart.setHoleRadius(58f);
        // 设置空心圆的半径
        pieChart.setTransparentCircleRadius(61f);
        // 设置是否显示中间的文字
        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(false);

    }

    /**
     * 显示圆环
     *
     * @param yvals
     * @param colors
     */
    public void showRingPieChart(List<PieEntry> yvals, List<Integer> colors) {
        //数据集合
        PieDataSet dataset = new PieDataSet(yvals, "");
        //填充每个区域的颜色
        dataset.setColors(colors);

        //dataset.setSelectionShift(5f);

        PieData pieData = new PieData(dataset);
        pieData.setValueFormatter(new PercentFormatter());
        pieChart.setData(pieData);

    }
}
