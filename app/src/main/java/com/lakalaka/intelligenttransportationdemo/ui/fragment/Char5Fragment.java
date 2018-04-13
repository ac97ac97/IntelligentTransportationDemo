package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.ChartDataBean;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakalaka on 2018/3/19/0019.
 */

public class Char5Fragment extends Fragment{

    private BarChart mBcTable4;

    private List<String> xValue;
    private List<BarEntry> yValue;

    private float data[][];
    private boolean isReturn=false;
    public Char5Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chart5,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initXValue();
        float value[][]=new float[2][2];
        for (int i = 0; i < value.length; i++) {
            for (int j = 0; j < value[i].length; j++) {
                value[i][j]=-1;
            }
        }
        setChart(getData(value));
    }

    private void setChart(BarData barData) {
        mBcTable4.getAxisRight().setEnabled(false);
        mBcTable4.getXAxis().setGridColor(Color.TRANSPARENT);
        mBcTable4.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mBcTable4.setDescription("");
        mBcTable4.setExtraOffsets(0f,20f,0f,20f);
        Legend legend=mBcTable4.getLegend();
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        nitifyChart(barData);
    }

    private void nitifyChart(BarData barData) {
        mBcTable4.setData(barData);
        mBcTable4.postInvalidate();
    }


    private void initXValue() {
        xValue=new ArrayList<>();
        xValue.add("女性");
        xValue.add("男性");
    }

    private void initView() {
        mBcTable4=getView().findViewById(R.id.bc_table4);

    }

    public BarData getData(float value[][]) {
        yValue=new ArrayList<>();
        if (value[0][0]!=-1)
            for (int i = 0; i < value.length; i++) {
                yValue.add(new BarEntry(value[i],i));
            }
        BarDataSet barDataSet=new BarDataSet(yValue,"");
        barDataSet.setStackLabels(new String[]{"有违章","无违章"});
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.parseColor("#eb7209"));
        colors.add(Color.parseColor("#6a9801"));
        barDataSet.setColors(colors);
        barDataSet.setValueTextColor(Color.parseColor("#c87d92"));
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueFormatter(new MyValueF());
        barDataSet.setBarSpacePercent(25f);

        BarData barData=new BarData(xValue,barDataSet);
        return barData;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            initData();
        }
    }

    private void initData() {
        new NetRequest("man.live")
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if("成功".equals(jsonObject.getString("response"))){
                                JSONObject jsonObj=jsonObject.getJSONObject("result");
                                ChartDataBean dataBean=new Gson().fromJson(jsonObj.toString(),ChartDataBean.class);
                                data=new float[2][2];
                                data[0][0]= (float) dataBean.getOne();
                                data[0][1]= (float) dataBean.getTwo();

                                data[1][0]= (float) dataBean.getThree();
                                data[1][1]= (float) dataBean.getFour();

                                nitifyChart(getData(data));
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

    private class MyValueF implements ValueFormatter {
        DecimalFormat decimalFormat;

        public MyValueF() {
            decimalFormat=new DecimalFormat("######0.0");
        }

        @Override
        public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
            int index=entry.getXIndex();
            if(isReturn){
                float value=data[index][1]/(data[index][0]+data[index][1]);
                isReturn=false;
                return decimalFormat.format(value)+"%";
            }else{
                isReturn=true;
                return "";
            }
        }
    }
}
