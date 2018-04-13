package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.app.AppClient;
import com.lakalaka.intelligenttransportationdemo.view.ZoomImageView;

/**
 *  模拟交通摄像头 显示违章信息中某一张图片
 */

public class ImageReadActivity extends AppCompatActivity {

    private Button mButton;
    private ZoomImageView mZoomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_read);
//        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initEvent() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageReadActivity.this.finish();
                startActivity(new Intent(ImageReadActivity.this,QueryResultsActivity.class));
            }
        });
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.btn_back);
        mZoomImageView = (ZoomImageView) findViewById(R.id.zoom_image);
        mZoomImageView.setImage(AppClient.illegaImageBitmap);

    }

}
