package com.lakalaka.intelligenttransportationdemo.Fragment2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.BusInfoBean;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakalaka on 2018/3/29/0029.
 */

public class busFragment extends Fragment {
    private BarChart busBarChart;
    private List<String> xValues;
    private List<BarEntry> yValues;
    private List<BarEntry> yValues2;
    private NetRequest busRequest;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bus_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        busBarChart = getView().findViewById(R.id.bus_barchart);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initXvalue();
        showChart(getSetBarData(new float[]{-1, 0},new float[]{0,1}));
    }

    private void initXvalue() {
        xValues = new ArrayList<>();
        xValues.add("一号公交");
        xValues.add("二号公交");
    }

    private BarData getSetBarData(float[] value,float[] value2 ) {
        yValues = new ArrayList<>();
        yValues2=new ArrayList<>();
        if(value[0]!=-1){
            for (int i = 0; i < value.length; i++) {
                yValues.add(new BarEntry(value[i],i));
            }
            for (int i = 0; i < value2.length; i++) {
                yValues2.add(new BarEntry(value2[i],i));
            }
        }

        BarDataSet barDataSet=new BarDataSet(yValues,"");
        BarDataSet barDataSet1=new BarDataSet(yValues2,"");
        if(value[0]<value[1]){
            barDataSet1.setColor(Color.GREEN);
            barDataSet.setColor(Color.YELLOW);
        }else{
            barDataSet1.setColor(Color.YELLOW);
            barDataSet.setColor(Color.GREEN);
        }




        ArrayList<BarDataSet> dataSets=new ArrayList<>();
        dataSets.add(barDataSet);
        dataSets.add(barDataSet1);

        BarData data=new BarData(xValues,dataSets);

        return data;

    }

    private void showChart(BarData barData) {
        busBarChart.setDescription("");
        busBarChart.getAxisRight().setEnabled(false);
        busBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        busBarChart.getXAxis().setGridColor(Color.TRANSPARENT);
        busBarChart.getLegend().setEnabled(false);
        notifyChart(barData);

    }

    private void notifyChart( BarData barData) {
        busBarChart.setData(barData);
        busBarChart.postInvalidate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (busRequest != null)
                busRequest.clean();
        } else {
            initData();
        }
    }



    private void initData() {
        busRequest = new NetRequest("distance.bus")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        if ("成功".equals(jsonObject.getString("response"))) {
                            jsonObject = jsonObject.getJSONObject("result");
                            BusInfoBean busInfoBean = new Gson().fromJson(jsonObject.toString(), BusInfoBean.class);
                            float a=busInfoBean.getOneBus_OneSite();
                            float b=busInfoBean.getOneBus_TwoSite();
                            float[] aa=new float[]{a,b};
                            float c=busInfoBean.getTwoBus_OneSite();
                            float d=busInfoBean.getTwoBus_TwoSite();
                            float[] bb=new float[]{c,d};
                            notifyChart(getSetBarData(aa,bb));
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }





}
