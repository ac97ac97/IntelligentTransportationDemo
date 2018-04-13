package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by lakalaka on 2018/3/20/0020.
 */

public class TemperatureFragment extends Fragment {


    private static final String TAG = "TemperatureFragment";
    private LineChart mLcTempTable;

    private List<String> xValues;
    private List<Entry> yValues;
    private List<Float> values;
    private List<Integer> colors;
    private int lineColorsG = Color.rgb(0, 255, 0);
    private int lineColorsR = Color.rgb(255, 0, 0);

    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
    Date curDate;
    String time;

    private NetRequest temperatureNetRequest;

    private Handler handler;

    public TemperatureFragment(Handler handler) {
        this.handler = handler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temperature, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
        initChart();
    }

    private void initData() {
        temperatureNetRequest = new NetRequest("sensor.live")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {

                        Log.i(TAG, "onSuccess: " + jsonObject);
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                int temperature = jsonObject.getJSONObject("result").getInt("temperature");
                                curDate = new Date(System.currentTimeMillis());
                                time = format.format(curDate);
                                notifyChart(getData(temperature, time));
                                float min = Collections.min(values);
                                float max = Collections.max(values);
                                Message msg = Message.obtain();
                                msg.what = LifeAssistantFragment.SEND_TEXT;
                                msg.obj = "过去一分钟最高气温:" + max + "°C  最低气温:" + min + "°C";
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }

                });
    }

    private void initChart() {
        setChart(getData(-1, null));
    }

    private void initView() {
        mLcTempTable = (LineChart) getView().findViewById(R.id.lc_temp_table);
    }


    private void setChart(LineData lineData) {

        mLcTempTable.setDescription("");
        mLcTempTable.getAxisRight().setEnabled(false);
        mLcTempTable.getXAxis().setLabelsToSkip(0);
        mLcTempTable.getXAxis().setGridColor(Color.TRANSPARENT);//去掉网格中竖线的显示
        mLcTempTable.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLcTempTable.getLegend().setEnabled(false);
        notifyChart(lineData);
    }

    private void notifyChart(LineData lineData) {
        mLcTempTable.setData(lineData);
        mLcTempTable.postInvalidate();
    }

    private LineData getData(float value, String t) {
        if (value == -1) {
            yValues = new ArrayList<>();
            values = new ArrayList<>();
            xValues = new ArrayList<>();
            colors=new ArrayList<>();
        }
        if (yValues.size() == 20) {
            yValues.remove(0);
            values.remove(0);
            xValues.remove(0);
            for (int i = 0; i < yValues.size(); i++) {
                Entry entry = yValues.get(i);
                entry.setXIndex(i);
            }
        }
        if (value != -1) {
            yValues.add(new Entry(value, yValues.size()));
            values.add(value);
            xValues.add(t);

        }
//        LineDataSet lineDataSet=new LineDataSet(yValues,"");
//        lineDataSet.setLineWidth(2.0f); // 线宽
//        lineDataSet.setColor(Color.RED);// 显示颜色
//        lineDataSet.setCircleSize(3f);// 显示的圆形大小
//        lineDataSet.setCircleColor(Color.RED);
//        lineDataSet.setDrawCircleHole(false);
//        LineData lineData=new LineData(xValues,lineDataSet);

        LineDataSet dataSet = new LineDataSet(yValues, "温度");

        if (value > 20) {
            colors.add(lineColorsR);
        } else if (value < 20&&value>0) {
            colors.add(lineColorsG);
        }
        dataSet.setColors(colors);
        dataSet.setCircleColors(colors);

        LineData lineData = new LineData(xValues, dataSet);

        return lineData;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (temperatureNetRequest != null)
                temperatureNetRequest.clean();
        } else {
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (temperatureNetRequest != null)
            temperatureNetRequest.clean();
    }
}