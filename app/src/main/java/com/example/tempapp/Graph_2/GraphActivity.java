package com.example.tempapp.Graph_2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tempapp.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean SQL_state = false;
    private ArrayList<DataCollection> data;
    DataCollection dc;

    private ArrayList<Entry> values = new ArrayList<>();
    private LineChart chart;
    private LineDataSet lineDataSet;

    GetMysql getMysql;

    Button btn_graph1, btn_graph2, btn_graph3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SQL_proc(SQL_state);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        init();
    }

    public void init() {
        chart = findViewById(R.id.linechart_1);

        btn_graph1 = findViewById(R.id.btn_graph1);
        btn_graph2 = findViewById(R.id.btn_graph2);
        btn_graph3 = findViewById(R.id.btn_graph3);

        btn_graph1.setOnClickListener(this);
        btn_graph2.setOnClickListener(this);
        btn_graph3.setOnClickListener(this);

        drawGraph(11);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_graph1:
                resetChart();
                drawGraph(1);
                break;
            case R.id.btn_graph2:
                resetChart();
                drawGraph(2);
                break;
            case R.id.btn_graph3:
                resetChart();
                drawGraph(3);
                break;
        }
    }

    public void resetChart() {
        values.clear();
        lineDataSet.removeFirst();
        chart.invalidate();
        chart.fitScreen();
        chart.getData().clearValues();
        chart.notifyDataSetChanged();
        chart.clear();
    }

    public void drawGraph(int n) {
        TextView txt = findViewById(R.id.txt_comment);

        for (int i=0; i<data.size(); i++) {
            dc = data.get(i);
            if (n == 11  || n == 1) {Log.e("NUM> ", n+"");
                values.add(new Entry(i, dc.getEar()));
            } else if (n == 2) {
                values.add(new Entry(i, dc.getShd()));
            } else if (n == 3) {
                values.add(new Entry(i, dc.getWst()));
            }
        }


        if (n == 1 || n == 11) {
            lineDataSet = new LineDataSet(values, "EAR");
            txt.setText("얼굴 비대칭");
        } else if (n == 2) {
            lineDataSet = new LineDataSet(values, "SHOULDER");
            txt.setText("어깨 비대칭");
        } else {
            lineDataSet = new LineDataSet(values, "WAIST");
            txt.setText("골반 비대칭");
        }

        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#ff9900"));
        lineDataSet.setCircleHoleColor(Color.WHITE);
        lineDataSet.setColor(Color.parseColor("#ff9900"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawValues(false);

        LineData lineData = new LineData(lineDataSet);
        chart.setData(lineData);

        // X축
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setGranularityEnabled(true);

        // Y축
        YAxis yLAxis = chart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = chart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        chart.setDoubleTapToZoomEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDescription(null);
        chart.animateY(500, Easing.EaseInCubic);
        chart.invalidate();

        MyMarkerView marker = new MyMarkerView(this, R.layout.marker_view);
        marker.setChartView(chart);
        chart.setMarker(marker);
    }


    // SQL 연동
    public void SQL_proc(boolean bool) {
        if (!bool) {
            getMysql = new GetMysql();
            getMysql.active = true;
            getMysql.start();

            getData();
        }
    }

    public void getData() {
        //getMysql.active = false;
        data = getMysql.return_dc();
    }
}
