package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.LifeItemAdapter;
import com.lakalaka.intelligenttransportationdemo.beans.LifeBean;
import com.lakalaka.intelligenttransportationdemo.beans.LifeInfoBean;
import com.lakalaka.intelligenttransportationdemo.beans.LifeUtils;
import com.lakalaka.intelligenttransportationdemo.beans.TemperatureRangeBean;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;
import com.lakalaka.intelligenttransportationdemo.ui.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class LifeAssistantFragment extends Fragment {


    public static final int SEND_TEXT = 0x01;
    private TextView tv_life_co;
    private TextView tv_life_info;
    private ImageView iv_life_finsh;
    private LineChart lc_wendu_table;
    private GridView hsv_list;
    private TextView tv_pm_text_info;
    private ViewPager vp_table;
    private LinearLayout ll_indicator;

    private float xMaxValue[];
    private float xMinValue[];

    private List<LifeBean> lifeBeens;

    private Fragment fragments[];

    private int icons[] = {R.drawable.zhiwaixianzhishu, R.drawable.ganmaozhisu, R.drawable.chuanyizhisu
            , R.drawable.yundongzhisu, R.drawable.kongqiwurankuoanzhisu};


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == SEND_TEXT) {
                String obj = (String) msg.obj;
                tv_pm_text_info.setText(obj);
            }
            return false;
        }
    });

    public LifeAssistantFragment() {
    }

    private LifeItemAdapter lifeItemAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_life_assistant, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initTemperatureData();
        initLineData();
        initLifeData();
        initAdapter();
        initEvent();
        initIndicator(0);
    }

    private void initAdapter() {
        lifeItemAdapter = new LifeItemAdapter(getActivity(), lifeBeens);
        hsv_list.setAdapter(lifeItemAdapter);
        vp_table.setAdapter(new ScollTableAdapter(getChildFragmentManager()));
    }

    private void initEvent() {
        iv_life_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTemperatureData();
                initLifeData();
                initLineData();
            }
        });
        vp_table.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initIndicator(position);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initIndicator(int index) {
        for (int i = 0; i < ll_indicator.getChildCount(); i++) {
            final int j = i;
            View view = ll_indicator.getChildAt(i);
            if (i == index) {
                view.setBackgroundResource(R.drawable.text_back);
            } else {
                view.setBackgroundColor(Color.TRANSPARENT);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vp_table.setCurrentItem(j, false);
                }
            });
        }
    }

    private void initLifeData() {
        new NetRequest("sensor.live")
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                JSONObject jsonObj = jsonObject.getJSONObject("result");
                                LifeInfoBean lifeInfoBean = new Gson().fromJson(jsonObj.toString(), LifeInfoBean.class);
                                lifeBeens = LifeUtils.getLifeUtils(lifeInfoBean);

                                lifeItemAdapter.setObjects(lifeBeens);
                                lifeItemAdapter.notifyDataSetChanged();
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

    private void initLineData() {
        new NetRequest("batch.live")
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                JSONObject jsonObj = jsonObject.getJSONObject("result");
                                TemperatureRangeBean rangeBean = new Gson().fromJson(
                                        jsonObj.toString(), TemperatureRangeBean.class);
                                xMaxValue[5] = rangeBean.getOneday_max();
                                xMinValue[5] = rangeBean.getOneday_min();

                                xMaxValue[4] = rangeBean.getTwoday_max();
                                xMinValue[4] = rangeBean.getTwoday_min();

                                xMaxValue[3] = rangeBean.getThreeday_max();
                                xMinValue[3] = rangeBean.getThreeday_min();

                                xMaxValue[2] = rangeBean.getTwoday_max();
                                xMinValue[2] = rangeBean.getTwoday_min();

                                xMaxValue[1] = rangeBean.getToday_max();
                                xMinValue[1] = rangeBean.getToday_min();

                                xMaxValue[0] = rangeBean.getYesterday_max();
                                xMinValue[0] = rangeBean.getYesterday_min();
                                showChart(lc_wendu_table, getLineData(xMaxValue, xMinValue));
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

    private void showChart(LineChart lineChart, LineData lineData) {


        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setEnabled(false);
        lineChart.setDescription("");
        lineChart.getXAxis().setGridColor(Color.TRANSPARENT);//去掉网格中竖线的显示
        lineChart.setData(lineData);

        Legend mLegend = lineChart.getLegend();

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setEnabled(false);
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.parseColor("#4151b1"));// 颜色
        lineChart.getXAxis().setTextColor(Color.parseColor("#4151b1"));
        /**
         * 设置X轴
         * */
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);//显示X轴
//        xAxis.setPosition(XAxis.XAxisPosition.TOP);//X轴位置
//        xAxis.setDrawGridLines(true);//设置x轴上每个点对应的线
//        xAxis.setSpaceBetweenLabels(2);
//        xAxis.setDrawGridLines(false);
//        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        lineChart.postInvalidate();


    }

    private void initTemperatureData() {
        new NetRequest("today.live")
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                JSONObject jsonObj = jsonObject.getJSONObject("result");
                                int temperature_current = jsonObj.getInt("temperature_current");
                                if (temperature_current >= 23) {


                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    //判断当前activity是否创建
                                    intent.putExtra("toValue", "href");


                                    PendingIntent pi = PendingIntent.getActivity(getContext(), 201, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    Notification notify = new NotificationCompat.Builder(getContext())
                                            .setContentTitle("温度过高警告")
                                            .setVibrate(new long[]{0, 1000})
                                            .setContentText("当前温度为" + temperature_current + "°C")
                                            .setWhen(System.currentTimeMillis())
                                            .setSmallIcon(R.mipmap.benci)
                                            .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                                    R.drawable.app))
                                            .setAutoCancel(true)
                                            .setContentIntent(pi)
                                            .build();

                                    notify.flags = Notification.FLAG_AUTO_CANCEL;

                                    NotificationManager manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

                                    manager.notify(1, notify);


                                }
                                int temperature_max = jsonObj.getInt("temperature_max");
                                int temperature_min = jsonObj.getInt("temperature_min");
                                tv_life_co.setText(temperature_current + "");
                                tv_life_info.setText("今天:" + temperature_min + "-" + temperature_max + "°C");
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

    private PM2_5Fragment pm2_5Fragment = new PM2_5Fragment(handler);
    private Co2Fragment co2Fragment = new Co2Fragment(handler);
    private LightFragment lightFragment = new LightFragment(handler);
    private TemperatureFragment temperatureFragment = new TemperatureFragment(handler);

    private void initData() {
        fragments = new Fragment[]{
                pm2_5Fragment,
                temperatureFragment,
                lightFragment,
                co2Fragment
        };
        xMaxValue = new float[6];
        xMinValue = new float[6];
        lifeBeens = new ArrayList<>();

    }

    private void initView() {
        tv_life_co = getView().findViewById(R.id.tv_life_co);
        tv_life_info = getView().findViewById(R.id.tv_life_info);
        iv_life_finsh = getView().findViewById(R.id.iv_life_finsh);
        lc_wendu_table = getView().findViewById(R.id.lc_wendu_table);
        hsv_list = getView().findViewById(R.id.hsv_list);
        tv_pm_text_info = getView().findViewById(R.id.tv_pm_text_info);
        vp_table = getView().findViewById(R.id.vp_table);
        vp_table.setOffscreenPageLimit(0);
        ll_indicator = getView().findViewById(R.id.ll_indicator);

    }


    private LineData getLineData(float[] maxValue, float[] minValue) {
        String[] xData = {"昨天", "今天", "明天", "周五", "周六", "周日"};
        ArrayList<String> xValue = new ArrayList<>();
        for (int i = 0; i < xData.length; i++) {
            xValue.add(xData[i]);
        }
        ArrayList<Entry> yMaxValue = new ArrayList<>();
        ArrayList<Entry> yMinValue = new ArrayList<>();
        for (int i = 0; i < xValue.size(); i++) {
            yMaxValue.add(new Entry(maxValue[i], i));
            yMinValue.add(new Entry(minValue[i], i));
        }
        LineDataSet lineDataSetMax = new LineDataSet(yMaxValue, "");
        // 用y轴的集合来设置参数

        lineDataSetMax.setLineWidth(2.0f); // 线宽
        lineDataSetMax.setColor(Color.RED);// 显示颜色
        lineDataSetMax.setCircleSize(3f);// 显示的圆形大小
        lineDataSetMax.setCircleColor(Color.RED);
        lineDataSetMax.setDrawCircleHole(false);
        lineDataSetMax.setDrawCubic(true);//允许设置平滑曲线


        LineDataSet lineDataSetMin = new LineDataSet(yMinValue, "");
        // 用y轴的集合来设置参数
        lineDataSetMin.setLineWidth(2.0f); // 线宽
        lineDataSetMin.setColor(Color.BLUE);// 显示颜色
        lineDataSetMin.setCircleSize(3f);// 显示的圆形大小
        lineDataSetMin.setCircleColor(Color.BLUE);
        lineDataSetMin.setDrawCircleHole(false);
        lineDataSetMin.setDrawCubic(true);

        List<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSetMax);
        lineDataSets.add(lineDataSetMin);
//        lineDataSet.setCircleColor(Color.TRANSPARENT);// 圆形的颜色
//        lineDataSet.setHighLightColor(Color.TRANSPARENT); // 点击后高亮的线的颜色
//        lineDataSet.setDrawValues(false);//隐藏折线图每个数据点的值
//        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
//        lineDataSets.add(lineDataSet); // add the datasets
//        lineDataSet.setDrawCircles(false);//图表上的数据点是否用小圆圈表示

//        lineDataSet.setDrawFilled(false);//是否填充折线下方
//        lineDataSet.setFillColor(Color.rgb(0, 255, 255));//折线图下方填充颜色设置
        LineData lineData = new LineData(xValue, lineDataSets);
        return lineData;
    }

    private class ScollTableAdapter extends FragmentPagerAdapter {

        public ScollTableAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
