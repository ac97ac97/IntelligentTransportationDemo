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
 * Created by lakalaka on 2018/3/29/0029.
 */

public class Co2Fragment2 extends Fragment {
    private LineChart co2Linechart;
    private List<String> xValues;
    private List<Entry> yValues;
    private List<Integer> colors;
    private SimpleDateFormat sf=new SimpleDateFormat("hh:mm:ss");
    private Date date;
    private String time;
    private int co2;
    private NetRequest Co2NetRequest;
    private LineData chart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.co2fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        co2Linechart=getView().findViewById(R.id.co2_linechar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChart();
    }

    private void initChart() {
        setChart(getChart(-1,null));
    }

    private void setChart(LineData lineData) {
        co2Linechart.setDescription("");
        co2Linechart.getAxisRight().setEnabled(false);
        co2Linechart.getXAxis().setGridColor(Color.TRANSPARENT);
        co2Linechart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        co2Linechart.getLegend().setEnabled(false);

        notifyChart(lineData);
    }

    private void notifyChart(LineData lineData) {
        co2Linechart.setData(lineData);
        co2Linechart.postInvalidate();
    }

    public LineData getChart(int value,String t) {
        if(value==-1){
            xValues=new ArrayList<>();
            yValues=new ArrayList<>();
            colors=new ArrayList<>();
        }
        if(yValues.size()>10){
            yValues.remove(0);
            xValues.remove(0);
            colors.remove(0);
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
            if(Co2NetRequest!=null){
                Co2NetRequest.clean();
            }
        }else{
            initData();
        }
    }

    private void initData() {
        Co2NetRequest=new NetRequest("sensor.live")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        date=new Date(System.currentTimeMillis());
                        time=sf.format(date);
                        if("成功".equals(jsonObject.getString("response"))){
                            co2=jsonObject.getJSONObject("result").getInt("co2");
                            notifyChart(getChart(co2,time));
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
