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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lakalaka on 2018/3/20/0020.
 */

public class LightFragment extends Fragment {

    private static final String TAG = "LightFragment";
    private LineChart mLcTempTable;

    private List<String> xValues;
    private List<Entry> yValues;
    private List<Float> values;

    private NetRequest temperatureNetRequest;

    private Handler handler;
    public LightFragment(Handler handler) {
        this.handler=handler;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_light, container, false);
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
        initXValue();
        initChart();
    }

    private void initData() {
        temperatureNetRequest=new NetRequest("sensor.live")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Log.i(TAG, "onSuccess: "+jsonObject);
                        try {
                            if ("成功".equals(jsonObject.getString("response"))){
                                int light=jsonObject.getJSONObject("result").getInt("light");
                                notifyChart(getData(light));
                                float max= Collections.max(values);
                                Message msg=Message.obtain();
                                msg.what=LifeAssistantFragment.SEND_TEXT;
                                msg.obj="过去一分钟最大相对湿度"+max+"%";
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
        setChart(getData(-1));
    }

    private void initView() {
        mLcTempTable = (LineChart) getView().findViewById(R.id.lc_light_table);
    }


    private void setChart(LineData lineData){
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

    private LineData getData(float value){
        if (value==-1){
            yValues=new ArrayList<>();
            values=new ArrayList<>();
        }
        if (yValues.size()==20){
            yValues.remove(0);
            values.remove(0);
            for (int i = 0; i <yValues.size(); i++) {
                Entry entry=yValues.get(i);
                entry.setXIndex(i);
            }
        }
        if (value!=-1){
            yValues.add(new Entry(value,yValues.size()));
            values.add(value);
        }
        LineDataSet lineDataSet=new LineDataSet(yValues,"");
        lineDataSet.setLineWidth(2.0f); // 线宽
        lineDataSet.setColor(Color.BLUE);// 显示颜色
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setCircleColor(Color.BLUE);
        lineDataSet.setDrawCircleHole(false);
        LineData lineData=new LineData(xValues,lineDataSet);
        return lineData;
    }

    private void initXValue(){
        xValues=new ArrayList<>();
        for (int i = 1; i <=60; i++) {
            if (i%3==0){
                String str=i+"";
                xValues.add("00".substring(str.length())+str);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            if (temperatureNetRequest!=null)
                temperatureNetRequest.clean();
        }else {
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (temperatureNetRequest!=null)
            temperatureNetRequest.clean();
    }
}