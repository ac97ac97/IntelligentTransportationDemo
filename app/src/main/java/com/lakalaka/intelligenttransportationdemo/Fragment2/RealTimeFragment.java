package com.lakalaka.intelligenttransportationdemo.Fragment2;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;
import com.lakalaka.intelligenttransportationdemo.ui.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lakalaka on 2018/3/28/0028.
 */

public class RealTimeFragment extends Fragment implements View.OnClickListener {


    private Button btn_wendu;
    private TextView wendu_text;
    private Button btn_pm25;
    private TextView pm25_text;
    private Button btn_light;
    private TextView light_text;
    private Button btn_co2;
    private TextView co2_text;
    private Button btn_status;
    private TextView status_text;
    private int temperature;
    private int co2;
    private int pm2;
    private int light;
    private int status;



    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                wendu_text.setText("当前温度:" + temperature);
                pm25_text.setText("当前Pm2.5:" + pm2);
                co2_text.setText("当前Co2:" + co2);
                light_text.setText("当前相对湿度:" + light);
                status_text.setText("当前路况:" + status);
            }

            return false;
        }
    });


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.toolbar.setVisibility(View.VISIBLE);
//        RealTimeAllFragment.ll_realtime_title.setVisibility(View.GONE);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_realtime, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        btn_wendu = getView().findViewById(R.id.btn_wendu);
        btn_wendu.setOnClickListener(this);
        wendu_text = getView().findViewById(R.id.wendu_text);
        btn_co2 = getView().findViewById(R.id.btn_co2);
        btn_co2.setOnClickListener(this);
        co2_text = getView().findViewById(R.id.co2_text);
        btn_pm25 = getView().findViewById(R.id.btn_pm25);
        btn_pm25.setOnClickListener(this);
        pm25_text = getView().findViewById(R.id.pm25_text);
        btn_light = getView().findViewById(R.id.btn_light);
        btn_light.setOnClickListener(this);
        light_text = getView().findViewById(R.id.light_text);
        btn_status = getView().findViewById(R.id.btn_status);
        btn_status.setOnClickListener(this);
        status_text = getView().findViewById(R.id.status_text);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

    }

    private NetRequest netRequestSensor;
    private NetRequest netRequestStatus;

    private void initData() {
        netRequestSensor = new NetRequest("sensor.live")
                .setLoop(true)
                .setLoopTime(5000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        if ("成功".equals(jsonObject.getString("response"))) {
                            temperature = jsonObject.getJSONObject("result").getInt("temperature");
                            co2 = jsonObject.getJSONObject("result").getInt("co2");
                            pm2 = jsonObject.getJSONObject("result").getInt("pm2");
                            light = jsonObject.getJSONObject("result").getInt("light");

                            if (temperature > 20) {
                                btn_wendu.setBackgroundColor(Color.RED);
                            } else {
                                btn_wendu.setBackgroundColor(Color.GREEN);
                            }
                            if (pm2 > 100) {
                                btn_pm25.setBackgroundColor(Color.RED);
                            } else {
                                btn_pm25.setBackgroundColor(Color.GREEN);
                            }
                            if (light > 2000) {
                                btn_light.setBackgroundColor(Color.RED);
                            } else {
                                btn_light.setBackgroundColor(Color.GREEN);
                            }
                            if (co2 > 3000) {
                                btn_co2.setBackgroundColor(Color.RED);
                            } else {
                                btn_co2.setBackgroundColor(Color.GREEN);
                            }
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
        netRequestStatus = new NetRequest("status.war")
                .setLoop(true)
                .setLoopTime(5000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        if ("成功".equals(jsonObject.getString("response"))) {
                            JSONObject jsonobj = jsonObject.getJSONObject("result");
                            status = jsonobj.getInt("one");
                            if (status > 3) {
                                btn_status.setBackgroundColor(Color.RED);
                            } else if (status < 3 && status > 0) {
                                btn_status.setBackgroundColor(Color.GREEN);
                            }

                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }



    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.btn_wendu:
                MainActivity.toolbar.setVisibility(View.GONE);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fl_fragment_group,
                        new RealTimeAllFragment(2));
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.btn_pm25:
                MainActivity.toolbar.setVisibility(View.GONE);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fl_fragment_group,
                        new RealTimeAllFragment(4));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.btn_light:
                MainActivity.toolbar.setVisibility(View.GONE);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fl_fragment_group,
                        new RealTimeAllFragment(1));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.btn_co2:
                MainActivity.toolbar.setVisibility(View.GONE);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).replace(R.id.fl_fragment_group,
                        new RealTimeAllFragment(0));
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.btn_status:
                MainActivity.toolbar.setVisibility(View.GONE);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fl_fragment_group,
                        new RealTimeAllFragment(3));
                ft.addToBackStack(null);
                ft.commit();
                break;

        }


    }


}
