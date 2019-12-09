package com.zydl.wealthcalculator.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.zydl.wealthcalculator.R;
import com.zydl.wealthcalculator.model.HistoryBean;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sch.
 * Date: 2019/11/27
 * description:
 */
public class MyMarkerView extends MarkerView {
    private TextView tvTime;
    private TextView tvAsset;
    private TextView tvChange;
    private ArrayList<HistoryBean> mHistoryBeans;

    public MyMarkerView(Context context, ArrayList<HistoryBean> historyBeans) {
        super(context, R.layout.marker_view);//这个布局自己定义
        tvTime = findViewById(R.id.tv_marker_time);
        tvAsset = findViewById(R.id.tv_marker_asset);
        tvChange = findViewById(R.id.tv_marker_change);
        this.mHistoryBeans = historyBeans;
    }

    //显示的内容
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int index = (int) e.getX();
        tvTime.setText(mHistoryBeans.get(index).getTime());
        tvAsset.setText(mHistoryBeans.get(index).getNum() + "元");
        double changeNum;
        if (index > 0) {
            changeNum = mHistoryBeans.get(index).getNum() - mHistoryBeans.get(index - 1).getNum();
        } else {
            changeNum = mHistoryBeans.get(index).getNum();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        if (changeNum > 0) {
            tvChange.setText("增长" + df.format(changeNum) + "元");
        } else if (changeNum == 0) {
            tvChange.setText("不变");
        } else {
            tvChange.setText("减少" + df.format(Math.abs(changeNum)) + "元");
        }

        super.refreshContent(e, highlight);
    }

    //标记相对于折线图的偏移量
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), 0);
    }


}