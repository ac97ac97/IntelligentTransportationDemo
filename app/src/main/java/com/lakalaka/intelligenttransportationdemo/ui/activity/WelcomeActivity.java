package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.lakalaka.intelligenttransportationdemo.R;


public class WelcomeActivity extends AppCompatActivity {

    private static final int TIME = 3000;
    private static final int GO_LOGIN = 100;
    private static final int GO_GUIDE = 101;

    private Context context;


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_LOGIN:
                    startActivity(new Intent(context,LoginActivity.class));
                    finish();
                    break;
                case GO_GUIDE:
                    startActivity(new Intent(context,GuideActivity.class));
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context=this;
        init();

    }

    private void init() {
        SharedPreferences sf=getSharedPreferences("data",MODE_PRIVATE);
        boolean isFirst=sf.getBoolean("isFirst",true);
        SharedPreferences.Editor editor=sf.edit();

        if (isFirst){
            editor.putBoolean("isFirst",false);
            mhandler.sendEmptyMessageDelayed(GO_GUIDE,TIME);
        }else{
            mhandler.sendEmptyMessageDelayed(GO_LOGIN,TIME);
        }
        editor.commit();
    }

}
