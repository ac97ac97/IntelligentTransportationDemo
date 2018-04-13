package com.lakalaka.intelligenttransportationdemo.network;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lakalaka.intelligenttransportationdemo.app.AppClient;
import com.lakalaka.intelligenttransportationdemo.app.AppConfig;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lakalaka on 2018/3/17/0017.
 */

public class NetRequest extends Thread{
    private JSONObject jsonBody;// 向服务器发送的数据
    private String action;// 地址的后缀
    private NetworkOnResult networkOnResult;// 成功或者失败的监听
    private boolean isLoop=false;//是否循环请求
    private long loopTime=1000;// 循环请求的间隔时间

    private ProgressDialog mProDialog;




    /**
     * 是否显示网络请求对话框
     * @param context
     * @return
     */
    public NetRequest showDialog(Context context){



        mProDialog=new ProgressDialog(context);
        mProDialog.setTitle("Loading");
        mProDialog.setMessage("请稍等");
        mProDialog.show();

        return this;
    }

    /**
     * 关闭请求进度对话框
     */

    public void cancelDialog(){
        if(mProDialog!=null){
            mProDialog.cancel();
        }
    }

    public NetRequest(String action) {
        this.action = action;
    }

    public NetRequest setJsonBody(JSONObject jsonBody){
        this.jsonBody=jsonBody;
        return this;
    }

    /**
     * 设置关于请求成功或者失败的监听
     * @param netWorkOnResult
     * @return
     */
    public NetRequest setNetWorkOnResult(NetworkOnResult netWorkOnResult){
        this.networkOnResult=netWorkOnResult;
        start();
        return this;
    }

    public NetRequest setLoop(boolean loop){
        isLoop=loop;
        return this;
    }

    public NetRequest setLoopTime(long loopTime){
        this.loopTime=loopTime;
        return this;
    }

    /**
     * 请求服务器数据
     */

    public void request(){
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL + action, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            networkOnResult.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                networkOnResult.onError();
            }
        });
        AppClient.getRequestmQueue().add(jsonRequest);
    }


    @Override
    public void run() {
        super.run();
        do {
            request();
            try {
                Thread.sleep(loopTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(isLoop);
        cancelDialog();
    }


    /**
     * 关闭循环获取数据的线程释放资源
     */

    public void clean(){isLoop=false;}

}
