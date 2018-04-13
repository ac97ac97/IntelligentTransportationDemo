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

public class StatusFragment extends Fragment {
    private LineChart statusLinechart;
    private List<String> xValues;
    private List<Entry> yValues;


    private NetRequest statusNetRequest;

    private int status;
    private String time;

    private SimpleDateFormat sf=new SimpleDateFormat("hh:mm:ss");
    private Date date;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.status_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        statusLinechart=getView().findViewById(R.id.status_linechar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initChart();

    }

    private void initData() {
        statusNetRequest=new NetRequest("status.war")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        date=new Date(System.currentTimeMillis());
                        time=sf.format(date);
                        if("成功".equals(jsonObject.getString("response"))){
                            JSONObject jsonObj=jsonObject.getJSONObject("result");
                            status=jsonObj.getInt("one");
                            notifyChart(getData(status,time));
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void initChart() {
        setChart(getData(-1,null));
    }

    private LineData getData(int value,String t) {
        if(value==-1) {
            yValues = new ArrayList<>();
            xValues = new ArrayList<>();
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

    private void setChart(LineData lineData) {
        statusLinechart.setDescription("");
        statusLinechart.getAxisRight().setEnabled(false);
        statusLinechart.getXAxis().setGridColor(Color.TRANSPARENT);
        statusLinechart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        statusLinechart.getLegend().setEnabled(false);
        notifyChart(lineData);
    }

    private void notifyChart(LineData lineData) {
        statusLinechart.setData(lineData);
        statusLinechart.postInvalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser) {
            if (statusNetRequest != null) {
                statusNetRequest.clean();
            }
        }else{
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(statusNetRequest!=null){
            statusNetRequest.clean();
        }
    }
}
