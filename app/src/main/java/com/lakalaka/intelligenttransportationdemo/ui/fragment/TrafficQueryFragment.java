package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/3/18.
 */

public class TrafficQueryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private int[] colors = {
            0,
            Color.parseColor("#6ab82e"),
            Color.parseColor("#ece93a"),
            Color.parseColor("#f49b25"),
            Color.parseColor("#e33532"),
            Color.parseColor("#b01e23"),
    };


    private TextView tv_traffic_huacheng1;
    private TextView tv_traffic_huacheng2;
    private TextView tv_traffic_huacheng3;
    private TextView tv_traffic_huacheng4;
    private TextView tv_traffic_xueyuan;
    private TextView tv_traffic_lianxiang;
    private TextView tv_traffic_xingfu;
    private TextView tv_traffic_yiyuan;
    private TextView tv_traffic_tingchechang;
    private TextView text_wendu;
    private TextView text_sidu;
    private TextView text_pm2_5;
    private TextView text_ymd;
    private TextView text_week;
    private ImageView iv_finshMap;

    public TrafficQueryFragment() {
    }

    public static TrafficQueryFragment newInstance(String mParam1, String mParam2) {
        TrafficQueryFragment fragment = new TrafficQueryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
        args.putString(ARG_PARAM2, mParam2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_traffic_query, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Object object);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDate();
        initData();
        initTraffic();
        initEvent();
    }

    private void initEvent() {
        iv_finshMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void initTraffic() {
        netRequestTraffic = new NetRequest("status.war")
                .setLoop(true)
                .setLoopTime(3000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        if ("成功".equals(jsonObject.getString("response"))) {
                            JSONObject jsonObj = jsonObject.getJSONObject("result");
                            Log.i("Traffic", "onSuccess:jsonObj的值为 " +jsonObj);
                            int one = jsonObj.getInt("one");
                            int two = jsonObj.getInt("two");
                            int three = jsonObj.getInt("three");
                            int four = jsonObj.getInt("four");
                            int five = jsonObj.getInt("five");
                            int six = jsonObj.getInt("six");
                            int seven = jsonObj.getInt("seven");



//                            拥堵情况

                            tv_traffic_xueyuan.setBackgroundColor(colors[one]);
                            tv_traffic_lianxiang.setBackgroundColor(colors[two]);
                            tv_traffic_yiyuan.setBackgroundColor(colors[three]);
                            tv_traffic_xingfu.setBackgroundColor(colors[four]);
                            setHuanChengBack(colors[five]);
                            tv_traffic_huacheng4.setBackgroundColor(colors[six]);
                            tv_traffic_tingchechang.setBackgroundColor(colors[seven]);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void setHuanChengBack(int color) {
        tv_traffic_huacheng1.setBackgroundColor(color);
        tv_traffic_huacheng2.setBackgroundColor(color);
        tv_traffic_huacheng3.setBackgroundColor(color);
    }

    private void initDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfWeek = new SimpleDateFormat("EEEE");
        text_ymd.setText(sdf.format(date));
        text_week.setText(getWeekOfDate(date));
    }

    private NetRequest netRequestSensor;

    private NetRequest netRequestTraffic;

    private void initData() {
        netRequestSensor = new NetRequest("environment.war")
                .setLoopTime(3000)
                .setLoop(true)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                JSONObject jsonObj = jsonObject.getJSONObject("result");
                                String humidity = jsonObj.getString("humidity");
                                String pm2 = jsonObj.getString("pm2");
                                String temperature = jsonObj.getString("temperature");
                                text_wendu.setText("温度:" + temperature + "°C");
                                text_sidu.setText("湿度:" + humidity + "ug/m3");
                                text_pm2_5.setText("PM2.5:" + pm2 + "%");
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

    private void initView() {


        tv_traffic_huacheng1 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng1);
        tv_traffic_huacheng2 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng2);
        tv_traffic_huacheng3 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng3);
        tv_traffic_huacheng4 = (TextView) getView().findViewById(R.id.tv_traffic_huacheng4);
        tv_traffic_xueyuan = (TextView) getView().findViewById(R.id.tv_traffic_xueyuan);
        tv_traffic_lianxiang = (TextView) getView().findViewById(R.id.tv_traffic_lianxiang);
        tv_traffic_xingfu = (TextView) getView().findViewById(R.id.tv_traffic_xingfu);
        tv_traffic_yiyuan = (TextView) getView().findViewById(R.id.tv_traffic_yiyuan);
        tv_traffic_tingchechang = (TextView) getView().findViewById(R.id.tv_traffic_tingchechang);
        text_wendu = (TextView) getView().findViewById(R.id.text_wendu);
        text_sidu = (TextView) getView().findViewById(R.id.text_sidu);
        text_pm2_5 = (TextView) getView().findViewById(R.id.text_pm2_5);
        text_ymd = (TextView) getView().findViewById(R.id.text_ymd);
        text_week = (TextView) getView().findViewById(R.id.text_week);
        iv_finshMap = (ImageView) getView().findViewById(R.id.iv_finshMap);
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal= Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        netRequestSensor.clean();
        netRequestTraffic.clean();
    }
}
