package com.junker.custom.view.customview.piechart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.junker.custom.view.R;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

    private PieChartView mPieChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        mPieChartView = findViewById(R.id.pie_chart_view);

        initData();
    }

    private void initData() {
        List<SectorsData> mListSector = new ArrayList<>();
        mListSector.add(new SectorsData("红队", 3f));
        mListSector.add(new SectorsData("蓝队", 3f));
        mListSector.add(new SectorsData("黑1队", 1f));
        mListSector.add(new SectorsData("黄队1", 100f));
        mListSector.add(new SectorsData("紫队", 1f));
        mListSector.add(new SectorsData("白队", 1f));
        mListSector.add(new SectorsData("黑1队", 1f));
        mListSector.add(new SectorsData("黄队2", 100f));
        mListSector.add(new SectorsData("紫队", 1f));
        mListSector.add(new SectorsData("白队", 1f));
        mListSector.add(new SectorsData("黑1队", 1f));
        mListSector.add(new SectorsData("黄队3", 100f));
        mListSector.add(new SectorsData("绿1队", 3f));
        mListSector.add(new SectorsData("绿2队", 3f));
        mListSector.add(new SectorsData("绿3队", 100f));
        mListSector.add(new SectorsData("紫队", 1f));
        mListSector.add(new SectorsData("白队", 1f));
        mListSector.add(new SectorsData("黑1队", 1f));

        mPieChartView.initData(mListSector);
    }
}