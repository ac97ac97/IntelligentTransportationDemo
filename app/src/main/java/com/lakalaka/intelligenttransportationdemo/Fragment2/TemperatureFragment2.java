package com.lakalaka.intelligenttransportationdemo.Fragment2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Date;
import java.util.List;

/**
 * Created by lakalaka on 2018/3/28/0028.
 */

public class TemperatureFragment2 extends Fragment {

    private LineChart lineChart;

    private NetRequest temperatureNetRequest;

    private List<String> xValues;
    private List<Entry> yValues;

    SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss");
    Date date;
    String time;
    int temperature;



    public TemperatureFragment2() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temperature2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        lineChart=getView().findViewById(R.id.temp_table2);

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChart();


    }

    private void initChart() {
        setChart(getData(-1,null));
    }

    private void setChart(LineData lineData) {
        lineChart.setDescription("");
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setLabelsToSkip(0);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setGridColor(Color.TRANSPARENT);
        lineChart.getLegend().setEnabled(false);
        notifyChart(lineData);
    }

    private void notifyChart(LineData lineData) {
        lineChart.setData(lineData);
        lineChart.postInvalidate();
    }

    private void initData() {
        temperatureNetRequest = new NetRequest("sensor.live")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        date = new Date(System.currentTimeMillis());
                        time = sf.format(date);
                        if ("成功".equals(jsonObject.getString("response"))) {
                            temperature = jsonObject.getJSONObject("result").getInt("temperature");
                            notifyChart(getData(temperature,time));
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public LineData getData(float value,String t) {
        if(value==-1){
            yValues=new ArrayList<>();
            xValues=new ArrayList<>();
        }
        if(yValues.size()==20){
            yValues.remove(0);
            xValues.remove(0);
            for (int i = 0; i < yValues.size(); i++) {
                Entry entry=yValues.get(i);
                entry.setXIndex(i);
            }
        }
        if(value!=-1){
            yValues.add(new Entry(value,yValues.size()));
            xValues.add(t);
        }

        LineDataSet lineDataSet=new LineDataSet(yValues,"");
        LineData lineData=new LineData(xValues,lineDataSet);
        return lineData;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            if(temperatureNetRequest!=null)
                temperatureNetRequest.clean();
        }else{
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(temperatureNetRequest!=null){
            temperatureNetRequest.clean();
        }
    }
}
