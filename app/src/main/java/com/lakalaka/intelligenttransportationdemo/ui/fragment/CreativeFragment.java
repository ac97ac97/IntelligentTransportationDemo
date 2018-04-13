package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.CreativeListAdapter;
import com.lakalaka.intelligenttransportationdemo.beans.ChartDataBean;
import com.lakalaka.intelligenttransportationdemo.beans.LampBean;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;
import com.lakalaka.intelligenttransportationdemo.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class CreativeFragment extends Fragment {

    private static final String TAG = "CreativeFragment";

    private List<LampBean> lampbeans = new ArrayList<>();

    List<ChartDataBean> status = new ArrayList<ChartDataBean>();

    List<Integer> a = new ArrayList<Integer>();
    String[] roads;


    private CreativeListAdapter itemAdapter;

    private ListView lvCreativeList;

    private NetRequest netRequestTraffic;
    private TextView tvLampLukouId;
    private TextView tvLampStatus;
    private TextView tvLampNumber;
    private TextView tvLampLvdeng;
    private TextView tvCreateLampTime;
    private TextView tvChangeTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creative, container, false);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initAdapter();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initTraffic();

    }


    /**
     * 加载路灯的时间
     */

    private void initData() {
        new NetRequest("all.light")
                .showDialog(getActivity())
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            Log.i(TAG, "onSuccess的json" + jsonObject);
                            if ("成功".equals(jsonObject.getString("response"))) {
                                //result是数组头   jsonarray是数组的形式
                                JSONArray jsonarray = jsonObject.getJSONArray("result");
                                Log.i(TAG, "onSuccess:解析出来的" + jsonarray);
                                lampbeans = GsonUtil.gsonTOList(jsonarray.toString(), LampBean.class);
                                Log.i(TAG, "lampBeans的值为 " + lampbeans);

                                itemAdapter.setObjects(lampbeans);
                                itemAdapter.notifyDataSetChanged();//获取数据并更新
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

    /**
     * 获得路口的名字和是否拥堵的状态
     */
    private void initTraffic() {

        netRequestTraffic = new NetRequest("status.war")
                .setLoop(true)
                .setLoopTime(5000)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) throws JSONException {
                        if ("成功".equals(jsonObject.getString("response"))) {
                            JSONObject jsonObj = jsonObject.getJSONObject("result");
                            Log.i(TAG, "onSuccess:jsonObj的值为 " + jsonObj);//

                            status = GsonUtil.gsonData(jsonObj, ChartDataBean.class);
                            Log.i(TAG, "charDatabean的内容为" + status);

                            String strStatus = status.toString();
                            Log.i(TAG, "strStatus: " + strStatus);
                            //通过substring的分隔去掉“[]”符号
                            String tempStatus = strStatus.substring(1, strStatus.length() - 1);
                            Log.i(TAG, "tempStatus: " + tempStatus);

                            roads = tempStatus.split(",");

                            itemAdapter.setStatus(status);
                            itemAdapter.notifyDataSetChanged();//获取数据并更新
                        }

                    }
                    @Override
                    public void onError() {

                    }
                });

    }


    private void initAdapter() {

        itemAdapter=new CreativeListAdapter(getActivity(),lampbeans,status);
        lvCreativeList.setAdapter(itemAdapter);
    }

    private void initView() {
        lvCreativeList = (ListView) getView().findViewById(R.id.lv_create_list);
    }
}
