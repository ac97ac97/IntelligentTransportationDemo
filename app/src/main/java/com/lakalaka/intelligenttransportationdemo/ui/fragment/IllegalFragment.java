package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;
import com.lakalaka.intelligenttransportationdemo.ui.activity.QueryResultsActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/3/18.
 * 车辆违章
 */

public class IllegalFragment extends Fragment {
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private String mParam1;
//    private String mParam2;
    private EditText edtCarNumber;
    private Button btnQueryCarNumber;

    public IllegalFragment() {
    }

    //@Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_illegal, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findInfo();
    }
    private void findInfo() {
        btnQueryCarNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String weizhangText = edtCarNumber.getText().toString().trim();
//                if (TextUtils.isEmpty(weizhangText)) {
//                    return;
//                }
                initTexts(weizhangText);
            }


        });
    }

    /**
     * 预先判断能否查询到数据如果可以 则携带数据跳转
     * @param infoText
     */
    private void initTexts(final String infoText) {
        final JSONObject jsonObj=new JSONObject();
        try {
            jsonObj.put("car_number","鲁"+infoText);
            new NetRequest("single.violation").setJsonBody(jsonObj).setNetWorkOnResult(new NetworkOnResult() {
                @Override
                public void onSuccess(JSONObject jsonObject) {
                    try {
                        if ("成功".equals(jsonObject.getString("response"))){
                            Intent intent = new Intent(getActivity(), QueryResultsActivity.class);
                            intent.putExtra("findInfo",infoText);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getActivity(),"没有查到"+"鲁"+infoText+"车的违章记录",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initViews() {
        edtCarNumber = getView().findViewById(R.id.edt_car_number);
        btnQueryCarNumber = getView().findViewById(R.id.btn_query_car_number);
    }
}
