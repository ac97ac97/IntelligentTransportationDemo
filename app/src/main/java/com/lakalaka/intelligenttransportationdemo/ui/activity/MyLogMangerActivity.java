package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;

public class MyLogMangerActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvZidong;
    private TextView tvShoudong;
    private TextView tvOpen;
    private TextView tvClose;
    private LinearLayout lilLudengKongZhi;
    private LinearLayout lilLudengMoshi;
    private Camera m_camera;
    private ImageView ivMyLogBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_log_manger);
        initViews();
    }

    private void initViews() {
        tvZidong = (TextView) findViewById(R.id.tv_zidong);
        tvShoudong = (TextView) findViewById(R.id.tv_shoudong);
        lilLudengMoshi = (LinearLayout) findViewById(R.id.tv_ludong_moshi);
        lilLudengMoshi.setOnClickListener(this);


        tvOpen = (TextView) findViewById(R.id.tv_open);
        tvClose = (TextView) findViewById(R.id.tv_close);
        lilLudengKongZhi = (LinearLayout) findViewById(R.id.tv_ludong_kongzhi);
        lilLudengKongZhi.setOnClickListener(this);

        ivMyLogBack = (ImageView) findViewById(R.id.iv_mylog_back);
        ivMyLogBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ludong_moshi:
                if (tvZidong.getVisibility() == View.INVISIBLE) {
                    tvZidong.setVisibility(View.VISIBLE);
                    tvShoudong.setVisibility(View.INVISIBLE);


                } else {
                    tvZidong.setVisibility(View.INVISIBLE);
                    tvShoudong.setVisibility(View.VISIBLE);
                    for (int i = 0; i <= 5; i++) {

                        try {
                            openLight();
                            Thread.sleep(1000);
                            closeLight();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    closeLight();

                }
                break;
            case R.id.tv_ludong_kongzhi:
                if (tvOpen.getVisibility() == View.INVISIBLE) {
                    tvOpen.setVisibility(View.VISIBLE);
                    tvClose.setVisibility(View.INVISIBLE);
                    openLight();
                } else {
                    tvOpen.setVisibility(View.INVISIBLE);
                    tvClose.setVisibility(View.VISIBLE);
                    closeLight();
                }
                break;
            case R.id.iv_mylog_back:
                startActivity(new Intent(this,MainActivity.class));
                break;
            default:
                break;
        }
    }

    private void openLight() {
        try {
            m_camera = Camera.open();
            Camera.Parameters mParameters;
            mParameters = m_camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            m_camera.setParameters(mParameters);
        } catch (Exception ex) {
        }
    }

    private void closeLight() {
        try {
            Camera.Parameters mParameters;
            mParameters = m_camera.getParameters();
            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            m_camera.setParameters(mParameters);
            m_camera.release();
        } catch (Exception ex) {
        }
    }
}
