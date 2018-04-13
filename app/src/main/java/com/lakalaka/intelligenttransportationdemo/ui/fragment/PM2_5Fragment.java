package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lakalaka on 2018/3/20/0020.
 */

public class PM2_5Fragment  extends Fragment{

    private BarChart mBcPm25Table;
    private ArrayList<String> xValue;
    private ArrayList<BarEntry> yValue;
    private NetRequest pm2Request;
    private ArrayList<Float> values;

    private Handler handler;
    public PM2_5Fragment(Handler handler){
        this.handler=handler;

    }

    public interface OnTextListener{
        void OnText(String text);
    }

    public static OnTextListener onTextListener;

    public static void setOnTextListener(OnTextListener onTextListener){
        PM2_5Fragment.onTextListener=onTextListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pm2_5,container,false);
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
        shWoChart(mBcPm25Table,getSetBarData(-1));
    }

    private BarData getSetBarData(float value){
        if (value==-1){
            yValue=new ArrayList<>();
            values=new ArrayList<>();
        }
        if (yValue.size()==20){
            yValue.remove(0);
            values.remove(0);
            for (int i = 0; i <yValue.size(); i++) {
                BarEntry barEntry=yValue.get(i);
                barEntry.setXIndex(i);
            }
        }
        if (value!=-1){
            yValue.add(new BarEntry(value,yValue.size()));
            values.add(value);
        }
        BarDataSet barDataSet=new BarDataSet(yValue,"");
        BarData barData=new BarData(xValue,barDataSet);
        return  barData;
    }
    private void shWoChart(BarChart barChar, BarData barData) {
        barChar.setDescription("");
        barChar.getAxisRight().setEnabled(false);
        barChar.getXAxis().setLabelsToSkip(0);//设置间隔
        barChar.getXAxis().setGridColor(Color.TRANSPARENT);

        XAxis xAxis=barChar.getXAxis();//获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴标签显示位置

        Legend legend=barChar.getLegend();
        legend.setEnabled(false);
        notifyChart(barChar,barData);

    }

    private void notifyChart(BarChart barChart,BarData barData) {
        barChart.setData(barData);
        barChart.postInvalidate();
    }

    private void initXValue() {
        xValue=new ArrayList<>();
        for (int i = 0; i <= 60; i++) {
            if(i%3==0){
                String str=i+"";
                xValue.add("00".substring(str.length())+str);
            }
        }
    }

    private void initView() {
        mBcPm25Table=getView().findViewById(R.id.bc_pm2_5_table);
    }

    /**
     * 当前界面销毁时调用
     * @param isVisibleToUser
     */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            if(pm2Request!=null)
                pm2Request.clean();
        }else{
            initData();
        }
    }

    private void initData() {
        pm2Request=new NetRequest("sensor.live")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        try {
                            if("成功".equals(jsonObject.getString("response"))){
                                JSONObject jsonObj=jsonObject.getJSONObject("result");
                                int pm2=jsonObj.getInt("pm2");
                                notifyChart(mBcPm25Table,getSetBarData(pm2));

                                float v= Collections.min(values);
                                Message msg=Message.obtain();
                                msg.what=LifeAssistantFragment.SEND_TEXT;
                                msg.obj="过去一分钟空气质量最差值"+v;
                                handler.sendMessage(msg);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

    }
}
