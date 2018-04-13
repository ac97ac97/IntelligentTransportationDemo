package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.BusDialogItemAdapter;
import com.lakalaka.intelligenttransportationdemo.adapter.BusListAdapter;
import com.lakalaka.intelligenttransportationdemo.beans.BusBean;
import com.lakalaka.intelligenttransportationdemo.beans.BusInfoBean;
import com.lakalaka.intelligenttransportationdemo.beans.BusSumBean;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 * 公交查询
 */

public class BusFindFragment extends Fragment {
    private TextView mtvChengzai;
    private Button mbtnXiangQing; //启动dialog 按钮
    /**
     * 此为 折叠的ListView
     */
    private ExpandableListView mElbusZhanlist;
    List<String> groupList;
    List<List<BusBean>> childList;
    /**
     * 折叠listview自定义适配器
     */
    private BusListAdapter busListAdapter;
    private NetRequest BusZhanTaiNet;
    private NetRequest BusCountNet;



    Button mBtnBack;  //从dialog返回主fragment
    ListView mLvBusInfo;//dialog 的数据显示的listview
    BusSumBean busSumBean; //Dialog 的实体类
    public BusFindFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_find, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mbtnXiangQing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        request();
    }

    //换算成米/分钟
    float dis = 1000 / 3f;

    private void request() {
        BusZhanTaiNet = new NetRequest("distance.bus")
                .setLoopTime(3000)
                .setLoop(true)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                jsonObject = jsonObject.getJSONObject("result");
                                //先解析出服务器返回数据
                                BusInfoBean busInfoBean = new Gson().fromJson(jsonObject.toString(), BusInfoBean.class);
                                List<BusBean> busList = new ArrayList<BusBean>();
                                busList.add(new BusBean("一号(" + busInfoBean.getOneBusPerson() + ")  "
                                        + ((int) (busInfoBean.getOneBus_OneSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getOneBus_OneSite() + "米"));
                                busList.add(new BusBean("二号(" + busInfoBean.getTwoBusPerson() + ")  "
                                        + ((int) (busInfoBean.getOneBus_TwoSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getOneBus_TwoSite() + "米"));
                                Collections.sort(busList);
                                childList.set(0, busList);
                                List<BusBean> busList2 = new ArrayList<BusBean>();
                                busList2.add(new BusBean("一号(" + busInfoBean.getOneBusPerson() + ")  "
                                        + ((int) (busInfoBean.getTwoBus_OneSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getTwoBus_OneSite() + "米"));
                                busList2.add(new BusBean("二号(" + busInfoBean.getTwoBusPerson() + ")  "
                                        + ((int) (busInfoBean.getTwoBus_TwoSite() / dis)) + "分钟到达"
                                        , "距离站台" + busInfoBean.getTwoBus_TwoSite() + "米"));
                                Collections.sort(busList2);
                                childList.set(1, busList2);

                                busListAdapter.setChildList(childList);
                                busListAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
        //获取承载人数
        BusCountNet = new NetRequest("passenger.bus").setLoop(true).setLoopTime(3000).setNetWorkOnResult(new NetworkOnResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    if ("成功".equals(jsonObject.getString("response"))) {
                        jsonObject = jsonObject.getJSONObject("result");
                        busSumBean = new Gson().fromJson(jsonObject.toString(), BusSumBean.class);
                        mtvChengzai.setText("当前承载能力" + busSumBean.getCount() + "人");
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

    private void initAdapter() {
        List<BusBean> beans = new ArrayList<>();

        beans.add(new BusBean("1号（101）人     5分钟到达", "距离站台100米"));
        beans.add(new BusBean("2号（101）人     5分钟到达", "距离站台1000米"));
        childList.add(beans);
        childList.add(beans);
        busListAdapter = new BusListAdapter(groupList, childList, getActivity());
        mElbusZhanlist.setAdapter(busListAdapter);
        /**
         * 共有2组 子 ListView
         */
        for (int i = 0; i < 2; i++) {
            mElbusZhanlist.expandGroup(i);
        }
    }

    private void showDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("公交车载客情况统计");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.bus_details_layout, null);//自定义dialog  view
        mLvBusInfo = view.findViewById(R.id.lv_bus_info);
        mBtnBack = view.findViewById(R.id.btn_back);
        TextView tvPersonNum = view.findViewById(R.id.tv_personNum);
        List<String> personNum = new ArrayList<>();
        if (busSumBean != null) {
            personNum.add(busSumBean.getOneBus() + "");
            personNum.add(busSumBean.getTwoBus() + "");
            personNum.add(busSumBean.getThreeBus() + "");
            personNum.add(busSumBean.getFifteenBus() + "");
        }
        BusDialogItemAdapter busDialogItemAdapter = new BusDialogItemAdapter(getActivity(), personNum);
        mLvBusInfo.setAdapter(busDialogItemAdapter);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvPersonNum.setText(busSumBean.getCount() + "");
        alertDialog.setView(view);
        alertDialog.show();

    }

    /**
     * 插入数据  2个站点
     */
    private void initData() {
        groupList = new ArrayList<>();
        childList = new ArrayList<>();
        mElbusZhanlist.setGroupIndicator(null);//折叠listview若无child 折叠指标箭头  则不显示
        groupList.add("中医院站");
        groupList.add("联想大厦站");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusCountNet.clean();//释放资源
        BusZhanTaiNet.clean();
    }

    private void initView() {
        mtvChengzai = getView().findViewById(R.id.tv_chengzai);
        mbtnXiangQing = getView().findViewById(R.id.btn_xingqing);
        mElbusZhanlist = getView().findViewById(R.id.el_bus_zhanlist);
    }
}
