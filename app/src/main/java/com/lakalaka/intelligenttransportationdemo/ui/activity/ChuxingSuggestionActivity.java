package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ChuxingSuggestionActivity extends AppCompatActivity {


    private TextView tv_showPm25;
    private TextView tv_showWenDu;
    private TextView tv_showShiDu;
    private TextView tv_showSuggestion;
    private ImageView iv_chuxingSuggestion;
    private NetRequest netRequestSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuxing_suggestion);
        initViews();
        initData();
    }
    private void initViews(){
        tv_showPm25 = (TextView) findViewById(R.id.tv_showPM2_5);
        tv_showWenDu = (TextView) findViewById(R.id.tv_showWenDu);
        tv_showShiDu = (TextView) findViewById(R.id.tv_showShiDu);
        tv_showSuggestion = (TextView) findViewById(R.id.tv_showSuggestion);

        iv_chuxingSuggestion = (ImageView) findViewById(R.id.iv_chuxing_suggestion_back);
        iv_chuxingSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChuxingSuggestionActivity.this,MainActivity.class));
            }
        });
    }
    private void initData(){
        netRequestSensor=new NetRequest("environment.war").setLoop(true).setLoopTime(3000).setNetWorkOnResult(new NetworkOnResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {
                if ("成功".equals(jsonObject.getString("response"))){
                    JSONObject jsonObj=jsonObject.getJSONObject("result");
                    String humidity=jsonObj.getString("humidity");
                    int int_humidity=Integer.parseInt(humidity);//题库要求湿度 0-80   数据范围10-30 （15-25）
                    String pm2=jsonObj.getString("pm2");
                    int int_pm2=Integer.parseInt(pm2);//题库要求 pm2 0-100  数据范围 20-40  （25-35）
                    String temperature=jsonObj.getString("temperature");//题库要求 温度 15-18   数据范围 40-70  （50-65）
                    int int_temperature=Integer.parseInt(temperature);
                    tv_showWenDu.setText(temperature+"℃");
                    tv_showShiDu.setText(humidity+"μg/m3");
                    tv_showPm25.setText(pm2+"%");
                    if ((int_humidity<15||int_humidity>25)&&(int_pm2<25||int_pm2>35)&&(int_temperature>65||int_temperature<50)){
                        tv_showSuggestion.setText("减少户外活动");
                    }else if ((int_humidity>=15&&int_humidity<=25)&&(int_pm2>=25&&int_pm2<=35)&&(int_temperature>=50&&int_temperature<=65)){
                        tv_showSuggestion.setText("快出去走走吧");
                    }else {
                        tv_showSuggestion.setText("可适当进行户外活动");
                    }
                }
            }

            @Override
            public void onError() {

            }
        });
    }
}
