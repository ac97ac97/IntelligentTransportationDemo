package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.app.AppClient;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ThresholdSettingFragment extends Fragment {
    private TextView mTvGetYuzi;
    private EditText mEtSetYuzi;
    private Button mBtnSetting;

    public ThresholdSettingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_threshold_setting,null);
        return view;
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
        initEvent();
    }

    private void initEvent() {
        mBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String money=mEtSetYuzi.getText().toString().trim();

                    JSONObject jsonObj=new JSONObject();
                    try {
                        jsonObj.put("CarValue",Integer.parseInt(money));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new NetRequest("setCarValue.in").setJsonBody(jsonObj).setNetWorkOnResult(new NetworkOnResult() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {
                                if ("成功".equals(jsonObject.getString("response"))) {
                                       AppClient.setInt("money",Integer.parseInt(money));
                                    initData();
                                    Toast.makeText(getActivity(),"设置成功",Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(getActivity(),"设置失败",Toast.LENGTH_SHORT).show();
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
        });
    }

    private void initData() {
        new NetRequest("getCarValue.in").setNetWorkOnResult(new NetworkOnResult() {
            @Override
            public void onSuccess(JSONObject jsonObject){
                try {
                    if ("成功".equals(jsonObject.getString("response"))){
                       int result = jsonObject.getInt("result");
                        AppClient.setInt("money",result);
                        if (result==0){
                            mTvGetYuzi.setText("当前 1-4 号小车账户余额警告阈值未设置!");
                        }else{
                            mTvGetYuzi.setText("当前 1-4 号小车账户预科警告阈值为"+result+"元");
                        }

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
        mTvGetYuzi=getView().findViewById(R.id.tv_get_yuzhi);
        mEtSetYuzi=getView().findViewById(R.id.et_set_yuzhi);
        mBtnSetting=getView().findViewById(R.id.btn_setting);
    }
}
