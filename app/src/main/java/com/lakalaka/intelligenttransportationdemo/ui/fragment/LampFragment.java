package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.LampListItemAdapter;
import com.lakalaka.intelligenttransportationdemo.beans.LampBean;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;
import com.lakalaka.intelligenttransportationdemo.util.GsonUtil;
import com.lakalaka.intelligenttransportationdemo.util.SpinnerAdapterUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */
public class LampFragment extends Fragment {
    private static final String TAG = "LampFragment";
    private Spinner sLampSort;
    private Button btnFind;
    private Button btnBatchSetting;
    private ListView lvLampList;

    private String lampSortArr[] = {
            "路口升序", "路口降序",
            "红灯升序", "红灯降序",
            "绿灯升序", "绿灯降序",
            "黄灯升序", "黄灯降序"
    };
    private List<LampBean> lampBeans = new ArrayList<>();

    private LampListItemAdapter itemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //表格布局
        return inflater.inflate(R.layout.fragment_lamp, container, false);
    }

    /**
     * 加载控件
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initAdapter();
    }

    /**
     * 加载适配器
     */
    private void initAdapter() {
        itemAdapter = new LampListItemAdapter(getActivity(), lampBeans);
        lvLampList.setAdapter(itemAdapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated:2");
        initEvent();
        initData();
    }

    /**
     * 初始化列表
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
                                lampBeans = GsonUtil.gsonTOList(jsonarray.toString(), LampBean.class);

                                Log.i(TAG, "lampBeans的值为 " + lampBeans);
                                itemAdapter.setObjects(lampBeans);
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
     * 选项排序
     */
    private void initEvent() {

        sLampSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //通过选择spinner中的项目
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //排序对象
                LampBean.select = position;
                Collections.sort(lampBeans);//这里已经将其排序了，只是没有更新，点击查询之后才更新
                itemAdapter.setObjects(lampBeans);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        itemAdapter.setOnsettingListener(new LampListItemAdapter.OnsettingListener() {
            @Override
            public void OnSetting(View view, int pos) {
                ArrayList<Integer> arr = new ArrayList<>();
                arr.add(pos + 1);
                showSettingLampDialog(arr);//显示设置灯的对话框
            }
        });
        //查询监听器
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击刷新数据
                itemAdapter.notifyDataSetChanged();
            }
        });

        //批量设置的监听器
        btnBatchSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> arr = new ArrayList<Integer>();
                List<LampBean> list = itemAdapter.getSelectList();
                for (int i = 0; i < list.size(); i++) {
                    LampBean lampBean = list.get(i);
                    arr.add(lampBean.getId());
                }
                showSettingLampDialog(arr); //显示设置对话框
            }
        });


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView() {
        sLampSort = (Spinner) getView().findViewById(R.id.s_lamp_sort);
        //下拉伸展框添加适配器
        sLampSort.setAdapter(SpinnerAdapterUtils.getSpinnerAdapter(getContext(), lampSortArr));
        btnFind = (Button) getView().findViewById(R.id.btn_find);
        btnBatchSetting = (Button) getView().findViewById(R.id.btn_batch_setting);
        lvLampList = (ListView) getView().findViewById(R.id.lv_lamp_list);
    }


    /**
     * 显示设置时长对话框
     *
     * @param arr 存放要设置的路口
     */

    private void showSettingLampDialog(final ArrayList<Integer> arr) {

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("红路灯设置");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.lamp_dialog_layout, null);
        final EditText etGreenLamp = view.findViewById(R.id.et_set_green_lamp);
        final EditText etRedLamp = view.findViewById(R.id.et_set_red_lamp);
        final EditText etYelloLamp = view.findViewById(R.id.et_set_yello_lamp);
        Button btnDetermine = view.findViewById(R.id.btn_queding);
        Button btnCancel = view.findViewById(R.id.btn_quxiao);
        //对话框取消按钮
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String greenLamp = etGreenLamp.getText().toString().trim();
                String redLamp = etRedLamp.getText().toString().trim();
                String yelloLamp = etYelloLamp.getText().toString().trim();
                Log.i(TAG, "greenLamp的值为 " + greenLamp);
                Log.i(TAG, "redLamp的值为 " + redLamp);
                Log.i(TAG, "yellowLamp的值为 " + yelloLamp);


                final JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("red_light", redLamp);
                    jsonObj.put("green_light", greenLamp);
                    jsonObj.put("yellow_light", yelloLamp);
                    for (int i = 0; i < arr.size(); i++) {
                        int lamp = arr.get(i);
                        jsonObj.put("id" + lamp, "" + lamp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "onClick" + jsonObj);
                new NetRequest("batch.light")
                        .setJsonBody(jsonObj)
                        .setNetWorkOnResult(new NetworkOnResult() {
                            @Override
                            public void onSuccess(JSONObject jsonObject) {
                                Log.i(TAG, "onSuccess:de 这个个数 " + jsonObject);
                                try {
                                    if ("成功".equals(jsonObject.getString("response"))) {
                                        //这个没有数组直接解析
                                        String result = jsonObject.getString("result");
                                        Log.i(TAG, "result的值为" + result);
                                        Toast.makeText(getContext(), "" + result, Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                        initData();
                                    } else {
                                        Toast.makeText(getContext(), "修改失败", Toast.LENGTH_SHORT).show();
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
        alertDialog.setView(view);
        alertDialog.show();


    }
}
