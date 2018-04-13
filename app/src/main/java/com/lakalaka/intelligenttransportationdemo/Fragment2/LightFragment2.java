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

public class LightFragment2 extends Fragment {
    private LineChart lightLineChart;
    private List<String> xValues;
    private List<Entry> yValues;
    private NetRequest lightNetRequest;
    private SimpleDateFormat sf=new SimpleDateFormat("hh:mm:ss");
    private Date date;
    private String time;
    private int light;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.light_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        lightLineChart=getView().findViewById(R.id.light_linechart);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChart();
    }

    private void initChart() {
        setChat(getChart(-1,null));
    }

    private void setChat(LineData lineData) {
        lightLineChart.setDescription("");
        lightLineChart.getAxisRight().setEnabled(false);
        lightLineChart.getXAxis().setGridColor(Color.TRANSPARENT);
        lightLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lightLineChart.getLegend().setEnabled(false);
        notifyChart(lineData);
    }

    private void notifyChart(LineData lineData) {
        lightLineChart.setData(lineData);
        lightLineChart.postInvalidate();
    }


    public LineData getChart(int value,String t) {
        if(value==-1){
            xValues=new ArrayList<>();
            yValues=new ArrayList<>();
        }
        if(yValues.size()>20){
            yValues.remove(0);
            xValues.remove(0);
            for (int i = 0; i < yValues.size(); i++) {
                Entry entry=yValues.get(i);
                entry.setXIndex(i);
            }
        }
        if(value!=-1){
            xValues.add(t);
            yValues.add(new Entry(value,yValues.size()));
        }
        LineDataSet lineDataSet=new LineDataSet(yValues,"");
        LineData lineData=new LineData(xValues,lineDataSet);
        return lineData;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            if(lightNetRequest!=null){
                lightNetRequest.clean();
            }
        }else{
            initData();
        }
    }

    private void initData() {
        lightNetRequest=new NetRequest("sensor.live")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        date=new Date(System.currentTimeMillis());
                        time=sf.format(date);
                        if("成功".equals(jsonObject.getString("response"))){
                            light=jsonObject.getJSONObject("result").getInt("light");
                            notifyChart(getChart(light,time));
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lightNetRequest!=null){
            lightNetRequest.clean();
        }
    }
}
