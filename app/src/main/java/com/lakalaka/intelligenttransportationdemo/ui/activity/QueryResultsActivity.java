package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.FindInfoItem1Adapter;
import com.lakalaka.intelligenttransportationdemo.adapter.FindSelectInfoInfosAdapter;
import com.lakalaka.intelligenttransportationdemo.app.AppClient;
import com.lakalaka.intelligenttransportationdemo.beans.FaKuanInfoBean;
import com.lakalaka.intelligenttransportationdemo.beans.FindInfoBean;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 路况信息查询
 */

public class QueryResultsActivity extends AppCompatActivity implements FindInfoItem1Adapter.OnRemoveClickListener {
    private String infoText;
    private static final String TAG = "QueryResultsActivity";
    private ImageView mIvAddbutton;
    private ListView mLvFindList;
    private ListView mLvFindListInfo;
    private FindInfoItem1Adapter findInfoItem1Adapter;
    private FindSelectInfoInfosAdapter findSelectInfoInfosAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_results);
        getInfo();
        initText();
        initView();
        initEvent();
    }
    private void initEvent() {
//        mLvFindList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                List<FaKuanInfoBean> infos = listInfos.get(position);
//                findSelectInfoInfosAdapter.setObjects(infos);
//                findSelectInfoInfosAdapter.notifyDataSetChanged();
//            }
//        });
        mLvFindListInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryResultsActivity.this.finish();
                startActivity(new Intent(QueryResultsActivity.this, IllegalImageActivity.class));
            }
        });
        mIvAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryResultsActivity.this.finish();
                startActivity(new Intent(QueryResultsActivity.this, MainActivity.class));
            }
        });
    }

    private void initList() {
        findInfoItem1Adapter = new FindInfoItem1Adapter(AppClient.lists, this);
        mLvFindList.setAdapter(findInfoItem1Adapter);
        findInfoItem1Adapter.setOnRemoveClickListener(this);
        findSelectInfoInfosAdapter = new FindSelectInfoInfosAdapter(AppClient.listInfos.get(0), this);
        mLvFindListInfo.setAdapter(findSelectInfoInfosAdapter);
    }


    private void initText() {
        final JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("car_number", "鲁" + infoText);
            new NetRequest("single.violation")
                    .showDialog(this)
                    .setJsonBody(jsonObj)
                    .setNetWorkOnResult(new NetworkOnResult() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {
                                if ("成功".equals(jsonObject.getString("response"))) {
                                    Log.i(TAG, "onSuccess: jsonObj的值为" + jsonObj );

                                    jsonObject = jsonObject.getJSONObject("result");
                                    //解析json得到实体类
                                    FindInfoBean findInfoBean = new Gson().fromJson(jsonObject.toString(), FindInfoBean.class);
                                    AppClient.lists.add(findInfoBean);




                                    JSONArray jsonArr = jsonObject.getJSONArray("infos");
                                    ArrayList<FaKuanInfoBean> info = new ArrayList<>();
                                    Log.i(TAG, "onSuccess:info的值为 " + info);
                                    if (info == null) {
                                        AppClient.listInfos.add(info);
                                    } else {
                                        for (int i = 0; i < jsonArr.length(); i++) {
                                            JSONObject jsonObjects = jsonArr.getJSONObject(i);
                                            FaKuanInfoBean fakuanInfoBean = new Gson().fromJson(jsonObjects.toString(), FaKuanInfoBean.class);
                                            info.add(fakuanInfoBean);
                                            Log.i(TAG, "onSuccess: info添加之后的值为" + info);
                                        }
                                        AppClient.listInfos.add(info);
                                    }
                                }
                                initList();
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

    /**
     * 获取跳转后的信息
     */
    public void getInfo() {
        Intent intent = getIntent();
        infoText = intent.getStringExtra("findInfo");
    }

    private void initView() {
        mIvAddbutton = (ImageView) findViewById(R.id.iv_addbutton);
        mLvFindList = (ListView) findViewById(R.id.lv_find_list);
        mLvFindListInfo = (ListView) findViewById(R.id.lv_find_list_info);
    }
    @Override
    public void OnRemoveClick(int pos) {
        AppClient.lists.remove(pos);
        AppClient.listInfos.remove(pos);
        findInfoItem1Adapter.setObjects(AppClient.lists);
        findInfoItem1Adapter.notifyDataSetChanged();

    }
}
