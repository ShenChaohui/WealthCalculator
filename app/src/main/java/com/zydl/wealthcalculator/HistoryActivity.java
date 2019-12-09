package com.zydl.wealthcalculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.zydl.wealthcalculator.database.BaseDao;
import com.zydl.wealthcalculator.database.BaseDaoImpl;
import com.zydl.wealthcalculator.model.HistoryBean;
import com.zydl.wealthcalculator.view.MyMarkerView;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sch.
 * Date: 2019/11/27
 * description:
 */
public class HistoryActivity extends AppCompatActivity {
    private Context mContext;
    private LineChart mLineChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history);
        mContext = this;
        initLineChart();
        initData();
    }

    private void initLineChart() {
        mLineChart = findViewById(R.id.line_chart);
        Description description = new Description();
        description.setText("");
        mLineChart.setDescription(description);
        Legend legend = mLineChart.getLegend();
        legend.setEnabled(false);
        //设置是否可以拖拽
        mLineChart.setDragEnabled(true);
        // 设置 是否可以缩放
        mLineChart.setScaleEnabled(false);
        // 获取 x 轴
        XAxis xAxis = mLineChart.getXAxis();
        // 设置 x 轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 取消 垂直 网格线
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10);
        // 设置 x 轴 坐标旋转角度
        xAxis.setLabelRotationAngle(0f);

        xAxis.setGranularity(1);
        // 获取 右边 y 轴
        YAxis mRAxis = mLineChart.getAxisRight();
        // 隐藏 右边 Y 轴
        mRAxis.setEnabled(false);
        // 获取 左边 Y轴
        YAxis mLAxis = mLineChart.getAxisLeft();
        //  左边 Y轴 坐标线
        mLAxis.setDrawAxisLine(true);
        //  横向 网格线
        mLAxis.setDrawGridLines(true);
        mLAxis.setTextColor(Color.BLACK);
        mLAxis.setDrawLabels(true);
//        //设置XY轴动画
//        mLineChart.animateXY(1500, 1500,
//                Easing.EaseInSine, Easing.EaseInSine);
        mLineChart.setExtraOffsets(50, 30, 100, 50);
    }

    private void initData() {
        BaseDao<HistoryBean, Integer> dao = new BaseDaoImpl<>(mContext, HistoryBean.class);
        try {
            final ArrayList<HistoryBean> historyBeans = (ArrayList<HistoryBean>) dao.queryAll();
            final List<Entry> entries = new ArrayList<>();
            if (historyBeans != null && historyBeans.size() > 0) {
                for (int i = 0; i < historyBeans.size(); i++) {
                    entries.add(new Entry(i, (float) historyBeans.get(i).getNum()));
                }
                final LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setValueTextSize(15);
                //线条平滑
//                dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                //填充
                Drawable drawable = getResources().getDrawable(R.drawable.fade_blue,null);
                dataSet.setDrawFilled(true);
                dataSet.setFillDrawable(drawable);
                LineData lineData = new LineData(dataSet);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        return df.format(value);
                    }
                });
                mLineChart.getXAxis().setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int v = (int) value;
                        if (v < entries.size() && v >= 0) {
                            return historyBeans.get(v).getTime();
                        } else {
                            return "";
                        }
                    }
                });
                //设置一页最大显示个数为6，超出部分就滑动
                float ratio = (float) entries.size() / (float) 7;
                //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
                mLineChart.zoom(ratio, 1f, 0, 0);
                mLineChart.moveViewToX(entries.size());//移动到最后一个
                MyMarkerView markerView = new MyMarkerView(mContext,historyBeans);
                mLineChart.setMarker(markerView);
                mLineChart.setData(lineData);
                mLineChart.invalidate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
